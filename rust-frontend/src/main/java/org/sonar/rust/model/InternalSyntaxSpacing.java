package org.sonar.rust.model;

import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;

public class InternalSyntaxSpacing extends RustTree{
    private final int start;
    private final int end;

    public InternalSyntaxSpacing(int start, int end) {
        super(null);
        this.start = start;
        this.end = end;
    }

    @Override
    public Kind kind() {
        // FIXME should have a dedicated kind associated with a dedicated interface.
        return Tree.Kind.TRIVIA;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public Iterable<Tree> children() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(TreeVisitor visitor) {
        // Do nothing at the moment. Spacings are dropped anyway.
    }

    public int start() {
        return start;
    }

    public int end() {
        return end;
    }
}
