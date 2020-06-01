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
package org.elegoff.plugins.rust.rules;

import org.elegoff.plugins.rust.language.RustLanguage;
import org.sonar.api.server.rule.RulesDefinition;

public class RustRulesDefinition implements RulesDefinition {
    /**
     * Path to the directory/folder containing the descriptor files (JSON and HTML) for the rules
     */
    public static final String RULES_DEFINITION_FOLDER = "org/elegoff/l10n/rust/rules";


    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository("rust", RustLanguage.KEY).setName("RUST Analyzer");

        //Current repository is empty
        repository.done();
    }
}