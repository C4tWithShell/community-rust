package org.sonar.rust.tree;


import org.sonar.plugins.rust.api.tree.*;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileInputImpl extends RsTree implements FileInput {
    private final StatementList statements;
    private final Token endOfFile;
    private final StringLiteral docstring;


    public FileInputImpl(@Nullable StatementList statements, Token endOfFile, @Nullable StringLiteral docstring) {
        this.statements = statements;
        this.endOfFile = endOfFile;
        this.docstring = docstring;
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
