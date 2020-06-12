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
package org.sonar.rust.tree;


import org.sonar.plugins.rust.api.tree.*;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileInputImpl extends RsTree implements FileInput {
    private final StatementList statements;
    private final Token endOfFile;
    private final StringLiteral docstring;


    public FileInputImpl(@Nullable StatementList statements, Token endOfFile, @Nullable StringLiteral docstring) {
        this.statements = statements;
        this.endOfFile = endOfFile;
        this.docstring = docstring;
    }

    @Override
    public void accept(TreeVisitor visitor) {

    }

    @Override
    public boolean is(Kind... kinds) {
        return false;
    }

    @Override
    public Token firstToken() {
        return null;
    }

    @Override
    public Token lastToken() {
        return null;
    }

    @Override
    public Tree parent() {
        return null;
    }

    @Override
    public List<Tree> children() {
        return null;
    }

    @Override
    public Kind getKind() {
        return null;
    }
}
