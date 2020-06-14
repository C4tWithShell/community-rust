package org.sonar.rust.model;

import com.google.common.collect.Iterables;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;
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

    public int getLine() {
        SyntaxToken firstSyntaxToken = firstToken();
        if (firstSyntaxToken == null) {
            return -1;
        }
        return firstSyntaxToken.line();
    }

    public static class CompilationUnitTreeImpl extends RustTree implements CompilationUnitTree {

        private final List<Tree> types;
        @Nullable

        private final SyntaxToken eofToken;


        public CompilationUnitTreeImpl(List<Tree> types, SyntaxToken eofToken) {
            super(Kind.COMPILATION_UNIT);
            this.types = types;
            this.eofToken = eofToken;
        }

        @Override
        public Kind kind() {
            return Kind.COMPILATION_UNIT;
        }



        @Override
        public List<Tree> types() {
            return types;
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

                    types,

                    Collections.singletonList(eofToken));
        }


        @Override
        public SyntaxToken eofToken() {
            return eofToken;
        }

    }
}
