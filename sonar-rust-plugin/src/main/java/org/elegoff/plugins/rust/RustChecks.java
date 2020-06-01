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
package org.elegoff.plugins.rust;

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.elegoff.plugins.rust.api.RustCheck;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RustChecks {
    private final CheckFactory checkFactory;
    private List<Checks<RustCheck>> checksByRepository = new ArrayList<>();

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
