/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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
package org.sonar.rust.parser;

import com.sonar.sslr.api.AstNode;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class RustParserTest {

    private final RustParser parser = RustParser.create();

    @Test
    public void testParse() throws Exception {
        Collection<File> files = listFiles();
        AstNode node;
        for (File file : files) {
            String fileContent = new String(Files.readAllBytes(file.toPath()), UTF_8);
            node =parser.parse(fileContent);
            assertThat(node).isNotNull();
        }
    }

    private static Collection<File> listFiles() {
        File dir = new File("src/test/resources/parser/");
        return FileUtils.listFiles(dir, new String[]{"rs"}, true);
    }

}
