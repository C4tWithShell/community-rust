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
package org.sonar.rust.ast.parser;

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.model.RustTree;

public class RustGrammar {
    private final GrammarBuilder<InternalSyntaxToken> b;
    private final TreeFactory f;

    public RustGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
        this.b = b;
        this.f = f;
    }


    public RustTree.CompilationUnitTreeImpl COMPILATION_UNIT() {
        return b.<RustTree.CompilationUnitTreeImpl>nonterminal(org.sonar.rust.ast.parser.RustLexer.COMPILATION_UNIT)
                .is(
                        f.newCompilationUnit(
                               b.token(RustLexer.SPACING),

                                b.token(RustLexer.EXPRESSION),
                                b.token(org.sonar.rust.ast.parser.RustLexer.EOF)));
    }







}
