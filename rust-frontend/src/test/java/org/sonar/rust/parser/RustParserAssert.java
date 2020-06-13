package org.sonar.rust.parser;

import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.matcher.RuleDefinition;
import org.fest.assertions.GenericAssert;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.internal.vm.EndOfInputExpression;
import org.sonar.sslr.internal.vm.FirstOfExpression;
import org.sonar.sslr.internal.vm.lexerful.TokenTypeExpression;
import org.sonar.sslr.tests.Assertions;
import org.sonar.sslr.tests.ParsingResultComparisonFailure;

public class RustParserAssert extends GenericAssert<RustParserAssert, RustParser> {

    public static RustParserAssert assertThat(RustParser actual) {
        return new RustParserAssert(actual);
    }
    public RustParserAssert(RustParser actual) {
        super(RustParserAssert.class, actual);
    }

    private RustParser createParserWithEofMatcher() {
        RuleDefinition rule = actual.getRootRule();
        RuleDefinition endOfInput = new RuleDefinition(new EndOfInput())
                .is(new FirstOfExpression(EndOfInputExpression.INSTANCE, new TokenTypeExpression(GenericTokenType.EOF)));
        RuleDefinition withEndOfInput = new RuleDefinition(new WithEndOfInput(actual.getRootRule().getRuleKey()))
                .is(rule, endOfInput);

        RustParser parser = RustParser.create();
        parser.setRootRule(withEndOfInput);

        return parser;
    }

    /**
     * Verifies that the actual <code>{@link Parser}</code> fully matches a given input.
     * @return this assertion object.
     */
    public RustParserAssert matches(String input) {
        isNotNull();
        hasRootRule();
        RustParser parser = createParserWithEofMatcher();
        String expected = "Rule '" + getRuleName() + "' should match:\n" + input;
        try {
            parser.parse(input);
        } catch (RecognitionException e) {
            String actual = e.getMessage();
            throw new ParsingResultComparisonFailure(expected, actual);
        }
        return this;
    }

    /**
     * Verifies that the actual <code>{@link Parser}</code> not matches a given input.
     * @return this assertion object.
     */
    public RustParserAssert notMatches(String input) {
        isNotNull();
        hasRootRule();
        RustParser parser = createParserWithEofMatcher();
        try {
            parser.parse(input);
        } catch (RecognitionException e) {
            // expected
            return this;
        }
        throw new AssertionError("Rule '" + getRuleName() + "' should not match:\n" + input);
    }

    private void hasRootRule() {
        Assertions.assertThat(actual.getRootRule())
                .overridingErrorMessage("Root rule of the parser should not be null")
                .isNotNull();
    }

    private String getRuleName() {
        return actual.getRootRule().getName();
    }

    static class EndOfInput implements GrammarRuleKey {
        @Override
        public String toString() {
            return "end of input";
        }
    }

    static class WithEndOfInput implements GrammarRuleKey {
        private final GrammarRuleKey ruleKey;

        public WithEndOfInput(GrammarRuleKey ruleKey) {
            this.ruleKey = ruleKey;
        }

        @Override
        public String toString() {
            return ruleKey + " with end of input";
        }
    }

}
