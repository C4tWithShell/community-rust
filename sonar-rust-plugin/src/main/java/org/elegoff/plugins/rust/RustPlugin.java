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
