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

import com.google.common.collect.Iterables;
import com.sonar.sslr.api.typed.Optional;
import org.sonar.plugins.rust.api.tree.*;
import org.sonar.rust.tree.SyntaxToken;
import org.sonar.sslr.grammar.GrammarRuleKey;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RustTree implements Tree {
    @Nullable
    private Tree parent;

    protected GrammarRuleKey grammarRuleKey;

    private List<Tree> children;

    public RustTree(GrammarRuleKey grammarRuleKey) {
        this.grammarRuleKey = grammarRuleKey;
    }

    protected abstract Iterable<Tree> children();

    public boolean isLeaf() {
        return false;
    }

    @Override
    public Tree parent() {
        return parent;
    }


    @Override
    @Nullable
    public SyntaxToken firstToken() {
        for (Tree child : getChildren()) {
            SyntaxToken first = child.firstToken();
            if (first != null) {
                return first;
            }
        }
        return null;
    }

    @Override
    @Nullable
    public SyntaxToken lastToken() {
        List<Tree> trees = getChildren();
        for (int index = trees.size() - 1; index >= 0; index--) {
            SyntaxToken last = trees.get(index).lastToken();
            if (last != null) {
                return last;
            }
        }
        return null;
    }

    @Override
    public final boolean is(Kind... kinds) {
        Kind treeKind = kind();
        for (Kind kindIter : kinds) {
            if (treeKind == kindIter) {
                return true;
            }
        }
        return false;
    }

    public List<Tree> getChildren() {
        if(children == null) {
            children = new ArrayList<>();
            children().forEach(child -> {
                // null children are ignored
                if (child != null) {
                    children.add(child);
                }
            });
        }
        return children;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public int getLine() {
        SyntaxToken firstSyntaxToken = firstToken();
        if (firstSyntaxToken == null) {
            return -1;
        }
        return firstSyntaxToken.line();
    }

    public GrammarRuleKey getGrammarRuleKey() {
        return grammarRuleKey;
    }


    public static class NotImplementedTreeImpl extends AbstractTypedTree implements ExpressionTree {

        public NotImplementedTreeImpl() {
            super(Kind.OTHER);
        }

        @Override
        public Kind kind() {
            return Kind.OTHER;
        }

        @Override
        public void accept(TreeVisitor visitor) {
            visitor.visitOther(this);
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public Iterable<Tree> children() {
            throw new UnsupportedOperationException();
        }
    }

    public static class CompilationUnitTreeImpl extends RustTree implements CompilationUnitTree {



        private final SyntaxToken eofToken;
        private final SyntaxToken any;


        public CompilationUnitTreeImpl(SyntaxToken any, SyntaxToken eofToken) {
            super(Kind.COMPILATION_UNIT);
            this.eofToken = eofToken;
            this.any = any;
        }

        @Override
        public Kind kind() {
            return Kind.COMPILATION_UNIT;
        }





        @Override
        public void accept(TreeVisitor visitor) {
            visitor.visitCompilationUnit(this);
        }

        @Nullable
        @Override
        public Tree parent() {
            return null;
        }

        @Nullable
        @Override
        public SyntaxToken firstToken() {
            return null;
        }

        @Nullable
        @Override
        public SyntaxToken lastToken() {
            return null;
        }

        @Override
        public Iterable<Tree> children() {
             return Iterables.concat(
                     Collections.singletonList(any),

                    Collections.singletonList(eofToken));
        }


        @Override
        public SyntaxToken eofToken() {
            return eofToken;
        }

    }
}
