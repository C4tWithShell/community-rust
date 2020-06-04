package org.sonar.rust.tree;

import org.sonar.plugins.rust.api.tree.*;

import java.util.List;

public class StatementListImpl extends RsTree implements StatementList {

    private List<Statement> statements;

    public StatementListImpl(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public List<Statement> statements() {
        return statements;
    }

    @Override
    public void accept(TreeVisitor visitor) {

    }

    @Override
    public boolean is(Kind... kinds) {
        return false;
    }

    @Override
    public Token firstToken() {
        return null;
    }

    @Override
    public Token lastToken() {
        return null;
    }

    @Override
    public Tree parent() {
        return null;
    }

    @Override
    public List<Tree> children() {
        return null;
    }

    @Override
    public Kind getKind() {
        return null;
    }
}
