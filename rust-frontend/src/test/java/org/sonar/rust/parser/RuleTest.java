package org.sonar.rust.parser;

import org.sonar.sslr.grammar.GrammarRuleKey;



public abstract class RuleTest {

    protected RustParser p = RustParser.create();

    protected void setRootRule(GrammarRuleKey ruleKey) {
        p.setRootRule(p.getGrammar().rule(ruleKey));
    }
}

