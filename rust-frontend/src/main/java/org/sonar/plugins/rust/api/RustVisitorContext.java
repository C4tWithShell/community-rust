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

import com.sonar.sslr.api.RecognitionException;
import org.sonar.plugins.rust.api.tree.FileInput;

import java.io.File;

public class RustVisitorContext {
    private final FileInput rootTree;
    private final RustFile rustFile;
    private File workingDirectory = null;
    private final RecognitionException parsingException;

    public RustVisitorContext(FileInput rootTree, RustFile rustFile, File workDir) {
        this.rootTree = rootTree;
        this.rustFile = rustFile;
        this.workingDirectory = workDir;
        this.parsingException = null;
    }

    public RustVisitorContext(RustFile rustFile, RecognitionException parsingException) {
        this.rootTree = null;
        this.rustFile = rustFile;
        this.parsingException = parsingException;
    }

    public FileInput rootTree() {
        return rootTree;
    }
}
