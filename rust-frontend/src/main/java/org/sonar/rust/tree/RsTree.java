package org.sonar.rust.tree;

import org.sonar.plugins.rust.api.tree.Token;
import org.sonar.plugins.rust.api.tree.Tree;

import java.util.List;

public abstract class RsTree implements Tree {
    protected Token firstToken;
    protected Token lastToken;
    private List<Tree> childs;
    private Tree parent = null;

    protected RsTree() {
    }

    protected void setParent(Tree parent) {
        this.parent = parent;
    }
}
