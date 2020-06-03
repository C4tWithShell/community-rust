package org.sonar.rust.parser;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.Parser;
import org.sonar.rust.api.RustGrammar;
import org.sonar.rust.lexer.LexerState;
import org.sonar.rust.lexer.RustLexer;

public class RustParser extends Parser<Grammar> {
    private final LexerState lexerState;
    private final Lexer lexer;


    public RustParser() {
        super(RustGrammar.create());
        super.setRootRule(super.getGrammar().getRootRule());
        this.lexerState = new LexerState();
        this.lexer = RustLexer.create(lexerState);
    }



    public static RustParser create() {
        return new RustParser();
    }
}
