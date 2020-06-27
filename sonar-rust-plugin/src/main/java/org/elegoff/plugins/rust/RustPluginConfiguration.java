package org.elegoff.plugins.rust;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.sonar.api.config.Configuration;
import org.sonar.rust.RustParserConfiguration;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Set;

public class RustPluginConfiguration {
    private final Configuration configuration;

    public RustPluginConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }



    RustParserConfiguration getParserConfiguration(Charset charset) {
        return RustParserConfiguration.builder()
                .setCharset(charset)
                .build();
    }
}
