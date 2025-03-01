/*
 * Community Rust Plugin
 * Copyright (C) 2021-2025 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.communityrust.clippy;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;


import static org.assertj.core.api.Assertions.assertThat;

class FileAdjustorTest {

  private static final Path PROJECT_DIR = Paths.get("src", "test", "resources", "FileAdjustor");
  private static final String MODULE_KEY = "FileAdjustor";

  private FileAdjustor fileAdjustor;

  private SensorContextTester context;

  @BeforeEach
  void setup() {
    context = SensorContextTester.create(PROJECT_DIR);
    addInputFiles("main.rs", "subfolder/main.rs");
    fileAdjustor = FileAdjustor.create(context);
  }

  @Test
  void should_return_relative_path_when_all_files_are_known() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "main.rs"))).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "subfolder", "main.rs"))).isEqualTo(path("subfolder", "main.rs"));
  }

  @Test
  void should_return_relative_path_when_first_file_is_in_subfolder() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "subfolder", "main.rs"))).isEqualTo(path("subfolder", "main.rs"));
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "main.rs"))).isEqualTo("main.rs");
  }

  @Test
  void should_not_return_relative_when_files_are_unknown() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "unknown.rs"))).isEqualTo(path("foo", "bar", "FileAdjustor", "unknown.rs"));
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "subfolder", "unknown.rs"))).isEqualTo(path("foo", "bar", "FileAdjustor", "subfolder", "unknown.rs"));
  }

  @Test
  void should_return_relative_when_first_file_is_unknown() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "unknown.rs"))).isEqualTo(path("foo", "bar", "FileAdjustor", "unknown.rs"));
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "subfolder", "main.rs"))).isEqualTo(path("subfolder", "main.rs"));
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "main.rs"))).isEqualTo("main.rs");
  }

  @Test
  void should_return_relative_path_when_already_relative() {
    assertThat(fileAdjustor.relativePath("main.rs")).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath(path("subfolder", "main.rs"))).isEqualTo(path("subfolder", "main.rs"));
  }

  @Test
  void test() {
    assertThat(fileAdjustor.relativePath(path("unknown", "main.rs"))).isEqualTo(path("main.rs"));
    assertThat(fileAdjustor.relativePath("main.rs")).isEqualTo("main.rs");
  }

  @Test
  void should_return_relative_path_for_second_file_when_unknown() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileHandler", "main.rs"))).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileHandler", "subfolder", "unknown.rs"))).isEqualTo(path("foo", "bar", "FileHandler", "subfolder", "unknown.rs"));
  }

  @Test
  void should_return_relative_path_for_unix_path() {
    assertThat(fileAdjustor.relativePath("/foo/bar/FileAdjustor/main.rs")).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath("/foo/bar/FileAdjustor/subfolder/main.rs")).isEqualTo(path("subfolder", "main.rs"));
  }

  @Test
  void should_return_relative_path_for_fqn_windows_path() {
    assertThat(fileAdjustor.relativePath("C:\\foo\\bar\\FileAdjustor\\main.rs")).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath("C:\\foo\\bar\\FileAdjustor\\subfolder\\main.rs")).isEqualTo(path("subfolder", "main.rs"));
  }

  @Test
  void should_return_relative_path_for_relative_windows_path() {
    assertThat(fileAdjustor.relativePath("main.rs")).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath("subfolder\\main.rs")).isEqualTo("subfolder\\main.rs");
  }

  @Test
  void should_return_handle_shorter_path() {
    assertThat(fileAdjustor.relativePath(path("foo", "bar", "FileAdjustor", "main.rs"))).isEqualTo("main.rs");
    assertThat(fileAdjustor.relativePath(path("bar", "main.rs"))).isEqualTo(path("bar", "main.rs"));
  }

  private void addInputFiles(String... paths) {
    Arrays.stream(paths).forEach(path -> context.fileSystem().add(TestInputFileBuilder.create(MODULE_KEY, path).build()));
  }

  private static String path(String first, String... more) {
    return Path.of(first, more).toString();
  }

}
