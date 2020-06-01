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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.elegoff.plugins.rust.rules.RustRulesDefinition;
import org.slf4j.LoggerFactory;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.elegoff.plugins.rust.clippy.ClippySensor;
import org.elegoff.plugins.rust.clippy.ClippyRulesDefinition;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.plugins.rust.language.RustQualityProfile;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import org.sonar.api.resources.Qualifiers;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class RustPlugin implements Plugin {

    private static final String EXTERNAL_ANALYZERS_CATEGORY = "External Analyzers";
    private static final String RUST_SUBCATEGORY = "Rust";

    public RustPlugin(){
        // Disable INFO logs for Reflections (see )
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(org.reflections.Reflections.class).setLevel(Level.ERROR);
    }

    @Override
    public void define(Context context) {
        context.addExtension(RustLanguage.class);
        context.addExtension(RustQualityProfile.class);

        // Add plugin settings (file extensions, etc.)
        context.addExtensions(RustLanguageSettings.getProperties());
        context.addExtensions(RustRulesDefinition.class, RustSensor.class);

        // clippy rules
        context.addExtension(ClippySensor.class);
        context.addExtensions(
                PropertyDefinition.builder(ClippySensor.REPORT_PROPERTY_KEY)
                        .name("Clippy Report Files")
                        .description("Paths (absolute or relative) to json files with Clippy issues.")
                        .category(EXTERNAL_ANALYZERS_CATEGORY)
                        .subCategory(RUST_SUBCATEGORY)
                        .onQualifiers(Qualifiers.PROJECT)
                        .multiValues(true)
                        .build(),
                ClippyRulesDefinition.class);
    }
}
