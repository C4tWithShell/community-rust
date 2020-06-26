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
package org.sonar.plugins.rust.api;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;

import javax.annotation.Nullable;
import java.util.List;

public interface RustFileScannerContext {
    @Nullable
    Object getSemanticModel();


    CompilationUnitTree getTree();

    InputFile getInputFile();
    boolean fileParsed();
    /**
     * Lines of the currently analyzed file.
     * @return list of file lines.
     */
    List<String> getFileLines();

    /**
     * Content of the currently analyzed file.
     * @return the file content as a String.
     */
    String getFileContent();
}
