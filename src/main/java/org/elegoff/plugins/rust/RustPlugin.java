package org.elegoff.plugins.rust;

import org.elegoff.plugins.rust.rules.RustSensor;
import org.slf4j.LoggerFactory;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;
import org.elegoff.plugins.rust.clippy.ClippySensor;
import org.elegoff.plugins.rust.clippy.ClippyRulesDefinition;
import org.elegoff.plugins.rust.languages.RustLanguage;
import org.elegoff.plugins.rust.languages.RustQualityProfile;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import org.sonar.api.resources.Qualifiers;
import ch.qos.logback.classic.LoggerContext;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class RustPlugin implements Plugin {

    private static final String EXTERNAL_ANALYZERS_CATEGORY = "External Analyzers";
    private static final String RUST_SUBCATEGORY = "Rust";
    public static final String FILE_SUFFIXES_KEY = "sonar.rust.file.suffixes";

    public RustPlugin() {
        // Disable INFO logs for Reflections (see )
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    }

    @Override
    public void define(Context context) {
        context.addExtension(RustLanguage.class);
        context.addExtension(RustQualityProfile.class);

        // Add plugin settings (file extensions, etc.)
        context.addExtensions(RustLanguageSettings.getProperties());

        // clippy rules
        context.addExtension(ClippySensor.class);
        context.addExtensions(
                PropertyDefinition.builder(ClippySensor.REPORT_PROPERTY_KEY)
                        .name("Bandit Report Files")
                        .description("Paths (absolute or relative) to json files with Bandit issues.")
                        .category(EXTERNAL_ANALYZERS_CATEGORY)
                        .subCategory(RUST_SUBCATEGORY)
                        .onQualifiers(Qualifiers.PROJECT)
                        .multiValues(true)
                        .build(),
                ClippyRulesDefinition.class);
    }
}
