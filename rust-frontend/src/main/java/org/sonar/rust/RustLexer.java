package org.sonar.rust;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.ParserAdapter;

import java.nio.charset.Charset;

public enum RustLexer implements GrammarRuleKey {

    TOKENS;

    private static LexerlessGrammarBuilder create() {
        LexerlessGrammarBuilder b =RustGrammar.create();

        b.rule(TOKENS).is(RustGrammar.SPACING, b.zeroOrMore(RustGrammar.ANYTHING), RustGrammar.EOF);

        b.setRootRule(TOKENS);

        return b;
    }

    public static ParserAdapter createWithoutPreprocessor(Charset charset) {
        return new ParserAdapter(charset, create().build());
    }

    public static ParserAdapter create(RustParserConfiguration conf) {
        return new ParserAdapter(conf.getCharset(), create().build());
    }

}
