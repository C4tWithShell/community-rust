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

import org.sonar.rust.tree.SyntaxToken;
import org.sonar.rust.tree.SyntaxTrivia;
import org.sonar.sslr.grammar.GrammarRuleKey;

import javax.annotation.Nullable;

public interface Tree {

    boolean is(Kind... kinds);

    void accept(TreeVisitor visitor);

    @Nullable
    Tree parent();

    @Nullable
    SyntaxToken firstToken();

    @Nullable
    SyntaxToken lastToken();

    enum Kind implements GrammarRuleKey {
        COMPILATION_UNIT(CompilationUnitTree.class),
        EXPRESSION(ExpressionTree.class),
        OTHER(Tree.class),
        STRING_LITERAL(LiteralTree.class),

        TOKEN(SyntaxToken.class),

        TRIVIA(SyntaxTrivia.class)


        ;

        final Class<? extends Tree> associatedInterface;

        Kind(Class<? extends Tree> associatedInterface) {
            this.associatedInterface = associatedInterface;
        }

        public Class<? extends Tree> getAssociatedInterface() {
            return associatedInterface;
        }
    }

    Kind kind();
}
