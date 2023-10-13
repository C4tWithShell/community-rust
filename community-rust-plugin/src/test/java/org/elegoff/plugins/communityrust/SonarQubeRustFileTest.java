/**
 * Community Rust Plugin
 * Copyright (C) 2021-2023 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
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

import java.io.FileNotFoundException;
import java.net.URI;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.rust.RustFile;


import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SonarQubeRustFileTest {

  private final InputFile inputFile = mock(InputFile.class);

  @Rule
  ExpectedException thrown = ExpectedException.none();

  @Test
  void knowFile() throws Exception {
    when(inputFile.contents()).thenReturn("Success");
    when(inputFile.filename()).thenReturn("myfile");
    when(inputFile.uri()).thenReturn(new URI("uri"));
    RustFile rustFile = SonarQubeRustFile.create(inputFile);
    assertThat(rustFile.content()).isEqualTo("Success");
    assertThat(rustFile.name()).isEqualTo("myfile");
    assertThat(rustFile.uri()).isEqualTo(new URI("uri"));
  }

  @Test
  void unknownFile() throws Exception {
    when(inputFile.contents()).thenThrow(new FileNotFoundException());
    RustFile rustFile = SonarQubeRustFile.create(inputFile);
    thrown.expect(IllegalStateException.class);
    rustFile.content();
  }

}
