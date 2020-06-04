/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.rust;

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.rust.api.RustCheck;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class RustChecks {
    private final CheckFactory checkFactory;
    private final List<Checks<RustCheck>> checksByRepository = new ArrayList<>();

    RustChecks(CheckFactory checkFactory) {
        this.checkFactory = checkFactory;
    }

    public RustChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
        checksByRepository.add(checkFactory.<RustCheck>create(repositoryKey).addAnnotatedChecks(checkClass));

        return this;
    }


    public List<RustCheck> all() {
        return checksByRepository.stream().flatMap(c -> c.all().stream()).collect(Collectors.toList());
    }

    @Nullable
    public RuleKey ruleKey(RustCheck check) {
        return checksByRepository.stream().map(c -> c.ruleKey(check)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}



