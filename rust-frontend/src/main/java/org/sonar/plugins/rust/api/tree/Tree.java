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
package org.sonar.plugins.rust.api.tree;

import java.util.List;

public interface Tree {

    void accept(TreeVisitor visitor);

    boolean is(Kind... kinds);

    Token firstToken();

    /**
     * @return the last meaningful token of the Tree.
     * Separators of simple statements (semicolon and/or newline) are not be returned by this method.
     */
    Token lastToken();

    Tree parent();

    List<Tree> children();

    enum Kind {
        EXPRESSION_STMT(ExpressionStatement.class),
       FILE_INPUT(FileInput.class),
        STATEMENT_LIST(StatementList.class),
        STRING_LITERAL(StringLiteral.class),
        TOKEN(Token.class);
        final Class<? extends Tree> associatedInterface;
        Kind(Class<? extends Tree> associatedInterface) {
            this.associatedInterface = associatedInterface;
        }
    }

    Kind getKind();
}
