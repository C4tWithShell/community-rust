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
package org.sonar.rust.model;

import com.google.common.annotations.VisibleForTesting;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.resolve.SemanticModel;

import javax.annotation.Nullable;
import java.util.List;

public class DefaultRustFileScannerContext implements RustFileScannerContext {
    private final CompilationUnitTree tree;
    @VisibleForTesting
    private final SemanticModel semanticModel;
    private final SonarComponents sonarComponents;

    private final InputFile inputFile;
    private final boolean fileParsed;

    @Override
    @Nullable
    public Object getSemanticModel() {
        return semanticModel;
    }

    @Override
    public CompilationUnitTree getTree() {
        return null;
    }

    @Override
    public InputFile getInputFile() {
        return null;
    }


    @Override
    public boolean fileParsed() {
        return fileParsed;
    }

    @Override
    public List<String> getFileLines() {
        return null;
    }

    @Override
    public String getFileContent() {
        return null;
    }

    public DefaultRustFileScannerContext(CompilationUnitTree tree, InputFile inputFile, SemanticModel semanticModel, SonarComponents sonarComponents, boolean fileParsed) {
        this.tree = tree;
        this.inputFile = inputFile;
        this.semanticModel = semanticModel;
        this.sonarComponents = sonarComponents;
        this.fileParsed = fileParsed;
    }
}
