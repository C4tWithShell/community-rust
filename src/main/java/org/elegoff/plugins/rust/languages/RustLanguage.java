package org.elegoff.plugins.rust.languages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.elegoff.plugins.rust.rules.RustSensor;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;


/**
 * This class defines the Rust language.
 */
public final class RustLanguage extends AbstractLanguage {

    public static final String NAME = "Rust";
    public static final String KEY = "rust";

    private final Configuration config;
    private static final Logger LOGGER = Loggers.get(RustLanguage.class);

    public RustLanguage
            (Configuration config) {
        super(KEY, NAME);
        this.config = config;
    }

    @Override
    public String[] getFileSuffixes() {
        String[] suffixes = filterEmptyStrings(config.getStringArray(RustLanguageSettings.FILE_SUFFIXES_KEY));
        if (suffixes.length == 0) {
            suffixes = StringUtils.split(RustLanguageSettings.FILE_SUFFIXES_DEFAULT_VALUE, ",");
        }
        LOGGER.debug("Rust language file suffixes " + suffixes.length + " => " + suffixes[0]);
        return suffixes;
    }

    private String[] filterEmptyStrings(String[] stringArray) {
        List<String> nonEmptyStrings = new ArrayList<>();
        for (String string : stringArray) {
            if (StringUtils.isNotBlank(string.trim())) {
                nonEmptyStrings.add(string.trim());
            }
        }
        return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
    }

}
