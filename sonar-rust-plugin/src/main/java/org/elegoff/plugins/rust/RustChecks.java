/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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
package org.elegoff.plugins.rust;

import com.sonar.sslr.api.Grammar;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.rule.RuleKey;
import javax.annotation.CheckForNull;
import java.util.*;
import org.sonar.api.batch.rule.Checks;
import org.sonar.squidbridge.SquidAstVisitor;

public class RustChecks {
    private final CheckFactory checkFactory;
    private final Set<Checks<SquidAstVisitor<Grammar>>> checksByRepository = new HashSet<>();

    private RustChecks(CheckFactory checkFactory) {
        this.checkFactory = checkFactory;
    }

    public static RustChecks createRustCheck(CheckFactory checkFactory) {
        return new RustChecks(checkFactory);
    }

    @SuppressWarnings("rawtypes")
    public RustChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
        checksByRepository.add(checkFactory
                .<SquidAstVisitor<Grammar>>create(repositoryKey)
                .addAnnotatedChecks(checkClass));

        return this;
    }


    public List<SquidAstVisitor<Grammar>> all() {
        var allVisitors = new ArrayList<SquidAstVisitor<Grammar>>();

        for (var checks : checksByRepository) {
            allVisitors.addAll(checks.all());
        }

        return allVisitors;
    }

    @CheckForNull
    public RuleKey ruleKey(SquidAstVisitor<Grammar> check) {
        for (var checks : checksByRepository) {
            RuleKey ruleKey = checks.ruleKey(check);
            if (ruleKey != null) {
                return ruleKey;
            }
        }
        return null;
    }

    public Set<Checks<SquidAstVisitor<Grammar>>> getChecks() {
        return new HashSet<>(checksByRepository);
    }
}
