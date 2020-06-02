package org.elegoff.rust.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum RustPunctuator implements TokenType {

    // Basic arithmetic operators
    PLUS("+"),
    MINUS("-"),
    MUL("*"),
    DIV("/"),
    MODULO("%"),
    INCR("++"),
    DECR("--"),
    ASSIGN("="),
    // Comparison/relational operators
    EQ("=="),
    NOT_EQ("!="),
    LT("<"),
    GT(">"),
    LT_EQ("<="),
    GT_EQ(">="),
    // Logical operators
    NOT("!"),
    AND("&&"),
    OR("||"),
    // Bitwise Operators
    BW_NOT("~"),
    BW_AND("&"),
    BW_OR("|"),
    BW_XOR("^"),
    BW_LSHIFT("<<"),
    BW_RSHIFT(">>"),
    // Compound assignment operators
    PLUS_ASSIGN("+="),
    MINUS_ASSIGN("-="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    MODULO_ASSIGN("%="),
    BW_AND_ASSIGN("&="),
    BW_OR_ASSIGN("|="),
    BW_XOR_ASSIGN("^="),
    BW_LSHIFT_ASSIGN("<<="),
    BW_RSHIFT_ASSIGN(">>="),
    // Member and pointer operators
    ARROW("->"), // ARROW?
    DOT("."), // DOT?
    DOT_STAR(".*"), // DOT_MUL?
    ARROW_STAR("->*"), // ARROW_MUL?

    // Delimiters
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    DOUBLECOLON("::"),
    BR_LEFT("("),
    BR_RIGHT(")"),
    CURLBR_LEFT("{"),
    CURLBR_RIGHT("}"),
    SQBR_LEFT("["),
    SQBR_RIGHT("]"),
    // Other operators
    QUEST("?"),
    ELLIPSIS("...");

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
    public boolean hasToBeSkippedFromAst(AstNode node) {
        return false;
    }

}
