package org.sonar.rust.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;

import java.util.Map;

public class KindMaps {

    private final Map<GrammarRuleKey, Tree.Kind> literals;

    public KindMaps() {
        ImmutableMap.Builder<GrammarRuleKey, Tree.Kind> literalsBuilder = ImmutableMap.builder();

        literalsBuilder.put(RustTokenType.STRING_LITERAL, Tree.Kind.STRING_LITERAL);
        this.literals = literalsBuilder.build();

    }

    public Tree.Kind getLiteral(GrammarRuleKey grammarRuleKey) {
        return Preconditions.checkNotNull(literals.get(grammarRuleKey), "Mapping not found for literal %s", grammarRuleKey);
    }
}
