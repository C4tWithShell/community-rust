package org.sonar.rust.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum RustPunctuator implements TokenType {
    SEMICOLON(";");

    private final String value;

    RustPunctuator(String word) {
        this.value = word;
    }


    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean hasToBeSkippedFromAst(AstNode astNode) {
        return false;
    }
}
