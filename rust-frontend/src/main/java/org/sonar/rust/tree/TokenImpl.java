package org.sonar.rust.tree;


import com.sonar.sslr.api.TokenType;
import org.sonar.plugins.rust.api.tree.Token;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.plugins.rust.api.tree.TreeVisitor;

import java.util.List;


public class TokenImpl extends RsTree implements Token {
    private com.sonar.sslr.api.Token token;


    public TokenImpl(com.sonar.sslr.api.Token token) {
        this.token = token;
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

    @Override
    public String value() {
        return null;
    }

    @Override
    public int line() {
        return 0;
    }

    @Override
    public int column() {
        return 0;
    }

    @Override
    public TokenType type() {
        return null;
    }
}
