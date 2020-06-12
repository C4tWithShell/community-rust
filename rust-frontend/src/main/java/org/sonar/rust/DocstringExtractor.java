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
package org.sonar.rust;

import org.sonar.plugins.rust.api.tree.*;

import javax.annotation.Nullable;

public class DocstringExtractor {
    private DocstringExtractor() {
    }

    public static StringLiteral extractDocstring(@Nullable StatementList statements) {
        if (statements != null) {
            Statement firstStatement = statements.statements().get(0);
            if (firstStatement.is(Tree.Kind.EXPRESSION_STMT) && ((ExpressionStatement) firstStatement).expressions().size() == 1
                    && firstStatement.children().get(0).is(Tree.Kind.STRING_LITERAL)) {
                return (StringLiteral) firstStatement.children().get(0);
            }
        }
        return null;
    }
}
