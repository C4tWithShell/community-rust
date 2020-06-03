package org.sonar.rust.parser;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.Parser;
import org.sonar.rust.api.RustGrammar;
import org.sonar.rust.lexer.RustLexer;

import java.util.List;

public final class RustParser extends Parser<Grammar> {
    private final Lexer lexer;

    private RustParser() {
        super(RustGrammar.create());
        super.setRootRule(super.getGrammar().getRootRule());

        this.lexer = RustLexer.create();
    }

    public static RustParser create() {
        return new RustParser();
    }

    @Override
    public AstNode parse(String source) {
        lexer.lex(source);
        return super.parse(tokens());
    }

    private List<Token> tokens() {
        return  lexer.getTokens();
    }
}
