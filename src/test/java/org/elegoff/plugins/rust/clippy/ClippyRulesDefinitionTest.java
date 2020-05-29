/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.elegoff.plugins.rust.clippy;

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
        assertThat(repository.rules().size()).isEqualTo(381);

        RulesDefinition.Rule rule = repository.rule("clippy::absurd_extreme_comparisons");
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo("Checks for comparisons where one side of the relation is either the minimum or maximum value for its type");
        assertThat(rule.htmlDescription()).isEqualTo("See description of Clippy rule <code>clippy::absurd_extreme_comparisons</code> at" +
                " the <a href=\"https://rust-lang.github.io/rust-clippy/master/index.html#absurd_extreme_comparisons\">Clippy website</a>.");
        assertThat(rule.tags()).isEmpty();
    }
}
