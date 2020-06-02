package org.elegoff.rust.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum RustTokenType implements TokenType {

    NUMBER,
    STRING,
    CHARACTER,
    WS; // whitespace

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getValue() {
        return name();
    }

    @Override
    public boolean hasToBeSkippedFromAst(AstNode node) {
        return this == WS;
    }

}