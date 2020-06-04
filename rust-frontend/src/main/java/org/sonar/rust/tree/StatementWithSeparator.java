package org.sonar.rust.tree;

import com.sonar.sslr.api.AstNode;

import javax.annotation.Nullable;

public class StatementWithSeparator {
    private AstNode statement;
    private Separators separators;

    public StatementWithSeparator(AstNode statement, @Nullable Separators separators) {
        this.statement = statement;
        this.separators = separators == null ? Separators.EMPTY : separators;
    }

    public AstNode statement() {
        return statement;
    }

    public Separators separator() {
        return separators;
    }
}
