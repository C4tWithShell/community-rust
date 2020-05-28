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
}