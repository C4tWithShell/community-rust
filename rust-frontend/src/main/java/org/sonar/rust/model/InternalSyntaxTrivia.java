package org.sonar.rust.model;

import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;
import org.sonar.rust.tree.SyntaxTrivia;

public class InternalSyntaxTrivia extends RustTree implements SyntaxTrivia {

    private final String comment;
    private final int startLine;
    private final int column;

    public InternalSyntaxTrivia(String comment, int startLine, int column) {
        super(null);
        this.comment = comment;
        this.startLine = startLine;
        this.column = column;
    }

    @Override
    public String comment() {
        return comment;
    }

    @Override
    public int startLine() {
        return startLine;
    }

    @Override
    public Kind kind() {
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
        //FIXME do nothing
    }

    public static SyntaxTrivia create(String comment, int startLine, int column) {
        return new InternalSyntaxTrivia(comment, startLine, column);
    }

    @Override
    public int getLine() {
        return startLine;
    }

    @Override
    public int column() {
        return column;
    }
}