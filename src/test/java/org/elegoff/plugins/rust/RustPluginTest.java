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

import junit.framework.TestCase;
import org.elegoff.plugins.rust.clippy.ClippyRulesDefinition;
import org.junit.Test;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.internal.SonarRuntimeImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RustPluginTest extends TestCase {
    @Test
    public void testGetExtensions() {
        Version v79 = Version.create(7, 9);
        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(v79, SonarQubeSide.SERVER, SonarEdition.DEVELOPER);
        assertThat(extensions(runtime)).hasSize(7);
        assertThat(extensions(runtime)).contains(ClippyRulesDefinition.class);
        assertThat(extensions(SonarRuntimeImpl.forSonarLint(v79))).hasSize(7);
    }

    private static List extensions(SonarRuntime runtime) {
        Plugin.Context context = new Plugin.Context(runtime);
        new RustPlugin().define(context);
        return context.getExtensions();
    }
}
