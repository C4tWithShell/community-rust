/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
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