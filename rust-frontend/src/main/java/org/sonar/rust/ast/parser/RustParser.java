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


import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.model.RustTree;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.LinkedList;


public final class RustParser extends ActionParser<Tree> {
    private Deque<RustTree> parentList = new LinkedList<>();

    public RustParser(LexerlessGrammarBuilder grammarBuilder, Class<RustGrammar> rustGrammarClass, TreeFactory treeFactory, RustNodeBuilder rustNodeBuilder, org.sonar.rust.ast.parser.RustLexer compilationUnit) {
        super(StandardCharsets.UTF_8, grammarBuilder, rustGrammarClass, treeFactory, rustNodeBuilder, compilationUnit);
    }

    public static ActionParser<Tree> createParser() {
        return new RustParser(
                //RustLexer.createGrammarBuilder(),
                RustLexer.createGrammarBuilder(),
                RustGrammar.class,
                new TreeFactory(),
                new RustNodeBuilder(),
                org.sonar.rust.ast.parser.RustLexer.COMPILATION_UNIT);
    }

    @Override
    public Tree parse(File file) {
        return createParentLink((RustTree) super.parse(file));
    }

    @Override
    public Tree parse(String source) {
        return createParentLink((RustTree) super.parse(source));
    }

    private Tree createParentLink(RustTree topParent) {
        parentList.push(topParent);
        while (!parentList.isEmpty()) {
            RustTree parent = parentList.pop();
            if (!parent.isLeaf()) {
                for (Tree nextTree : parent.getChildren()) {
                    RustTree next = (RustTree) nextTree;
                    if (next != null) {
                        next.setParent(parent);
                        parentList.push(next);
                    }
                }
            }
        }
        return topParent;
    }
}
