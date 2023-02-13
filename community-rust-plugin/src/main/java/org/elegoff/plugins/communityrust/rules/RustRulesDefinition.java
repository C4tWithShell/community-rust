/**
 * Community Rust Plugin
 * Copyright (C) 2021-2023 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.communityrust.rules;

import java.util.List;
import org.elegoff.plugins.communityrust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public class RustRulesDefinition implements RulesDefinition {
  /**
   * Path to the directory/folder containing the descriptor files (JSON and HTML) for the rules
   */
  public static final String RESOURCE_BASE_PATH = "org/elegoff/I10n/rust/rules";

  private final SonarRuntime sonarRuntime;

  public RustRulesDefinition(SonarRuntime sonarRuntime) {
    this.sonarRuntime = sonarRuntime;
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context.createRepository(CheckList.REPOSITORY_KEY, RustLanguage.KEY).setName("Community RUST");
    List<Class<?>> checkClasses = CheckList.getRustChecks();
    RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, sonarRuntime);
    ruleMetadataLoader.addRulesByAnnotatedClass(repository, checkClasses);
    repository.rules().stream()
      .forEach(r -> r.setTemplate(false));
    repository.done();
  }
}
