package org.elegoff.plugins.rust.clippy;

import java.util.Arrays;
import java.util.List;

import org.elegoff.plugins.rust.languages.RustLanguage;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.ExternalRuleLoader;


import static org.elegoff.plugins.rust.clippy.ClippySensor.LINTER_KEY;
import static org.elegoff.plugins.rust.clippy.ClippySensor.LINTER_NAME;

public class ClippyRulesDefinition implements RulesDefinition {

  private static final String RULES_JSON = "org/elegoff/l10n/rust/rules/rust/clippy/rules.json";

  
  private static final String RULE_REPOSITORY_LANGUAGE = RustLanguage.KEY;

  private static final List<String> TEXT_FILE_EXTENSIONS = Arrays.asList(".rs");

  static final ExternalRuleLoader RULE_LOADER = new ExternalRuleLoader(LINTER_KEY, LINTER_NAME, RULES_JSON, RULE_REPOSITORY_LANGUAGE);

  

  @Override
  public void define(Context context) {
      RULE_LOADER.createExternalRuleRepository(context);
  }

  static boolean isTextFile(String file) {
    return TEXT_FILE_EXTENSIONS.stream().anyMatch(file::endsWith);
  }
}