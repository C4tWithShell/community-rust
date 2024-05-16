/**
 * Community Rust Plugin
 * Copyright (C) 2021-2024 Vladimir Shelkovnikov
 * https://github.com/C4tWithShell/community-rust
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
package org.elegoff.plugins.communityrust;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.elegoff.plugins.communityrust.language.RustLanguage;
import org.fest.assertions.Assertions;
import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RustcParsingTest {
  private final File dir = new File("src/test/resources/rustc");
  private FileLinesContext fileLinesContext;
  private SensorContextTester tester;
  private RustSensor sensor;

  void reinit() {
    tester = SensorContextTester.create(dir);

    MapSettings settings = CommunityRustPluginConfigurationTest.getDefaultSettings();
    tester.setSettings(settings);

    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    fileLinesContext = mock(FileLinesContext.class);
    when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
    ActiveRulesBuilder activeRuleBuilder = new ActiveRulesBuilder();
    CheckFactory checkFactory = new CheckFactory(activeRuleBuilder.build());
    sensor = new RustSensor(checkFactory, fileLinesContextFactory);
  }

  private DefaultInputFile addInputFile(String fileName) throws IOException {
    String content = new String(Files.readAllBytes(new File(dir, fileName).toPath()));
    Assertions.assertThat(content).isNotEmpty();
    DefaultInputFile inputFile = new TestInputFileBuilder(tester.module().key(), fileName)
      .setModuleBaseDir(tester.fileSystem().baseDirPath())
      .setType(InputFile.Type.MAIN)
      .setLanguage(RustLanguage.KEY)
      .setCharset(StandardCharsets.UTF_8)
      .initMetadata(content)
      .build();
    tester.fileSystem().add(inputFile);
    return inputFile;
  }

  private DefaultInputFile executeSensorOnSingleFile(String fileName) throws IOException {
    DefaultInputFile inputFile = addInputFile(fileName);
    sensor.execute(tester);
    return inputFile;
  }

  private void checkme(String testfile) throws IOException {
    reinit();
    DefaultInputFile inputFile = executeSensorOnSingleFile(testfile);
    verify(fileLinesContext).save();
    Assertions.assertThat(tester.allAnalysisErrors()).isEmpty();
  }

  @Test
  void DebugInfoTest() throws IOException {
    checkme("debuginfo/associated-types.rs");
    checkme("debuginfo/borrowed-enums.rs");
  }

  @Test
  void RunMakeFulldepsTest() throws IOException {
    checkme("run-make-fulldeps/atomic-lock-free/atomic_lock_free.rs");
  }

  @Test
  void UITest() throws IOException {
    checkme("ui/cfg/cfg-panic.rs");

    checkme("ui/borrowck/issue-88434-minimal-example.rs");
    checkme("ui/borrowck/issue-88434-removal-index-should-be-less.rs");
    checkme("ui/const-generics/generic_const_exprs/issue-85848.rs");
    checkme("ui/const-generics/issues/issue-79674.rs");

    checkme("ui/cfg/cfg-panic-abort.rs");
    checkme("ui/feature-gates/feature-gate-cfg-target-has-atomic-equal-alignment.rs");
    checkme("ui/fmt/format-args-capture.rs");
    checkme("ui/issues/issue-68696-catch-during-unwind.rs");
  }

}
