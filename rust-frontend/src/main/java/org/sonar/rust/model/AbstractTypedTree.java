package org.sonar.rust.model;

import org.sonar.sslr.grammar.GrammarRuleKey;

public abstract class AbstractTypedTree extends RustTree {
    public AbstractTypedTree(GrammarRuleKey grammarRuleKey) {
        super(grammarRuleKey);
    }
}
