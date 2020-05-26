package org.elegoff.plugins.rust.externalreport.clippy;

import org.elegoff.plugins.rust.clippy.ClippyRulesDefinition;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;

import static org.assertj.core.api.Assertions.assertThat;

public class ClippyRulesDefinitionTest {

    @Test
    public void clippyExternalRepository() {
        RulesDefinition.Context context = new RulesDefinition.Context();
        ClippyRulesDefinition rulesDefinition = new ClippyRulesDefinition();
        rulesDefinition.define(context);

        assertThat(context.repositories()).hasSize(1);
        RulesDefinition.Repository repository = context.repository("external_clippy");
        assertThat(repository).isNotNull();
        assertThat(repository.name()).isEqualTo("Clippy");
        assertThat(repository.language()).isEqualTo("rust");
        assertThat(repository.isExternal()).isEqualTo(true);
        assertThat(repository.rules().size()).isEqualTo(387);

        RulesDefinition.Rule rule = repository.rule("clippy::absurd_extreme_comparisons");
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo("Checks for comparisons where one side of the relation is either the minimum or maximum value for its type");
        assertThat(rule.htmlDescription()).isEqualTo("See description of Clippy rule <code>clippy::absurd_extreme_comparisons</code> at" +
                " the <a href=\"https://rust-lang.github.io/rust-clippy/master/index.html#absurd_extreme_comparisons\">Clippy website</a>.");
        assertThat(rule.tags()).isEmpty();
    }
}
