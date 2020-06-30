package org.elegoff.plugins.rust;

import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import org.junit.Test;
import org.sonar.api.config.internal.MapSettings;

import java.nio.charset.Charset;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class RustPluginConfigurationTest {
    @Test
    public void getParserConfigurationCharset() {
        RustPluginConfiguration pluginConf = new RustPluginConfiguration(getDefaultSettings().asConfig());

        Charset charset = mock(Charset.class);
        assertThat(pluginConf.getParserConfiguration(charset).getCharset()).isEqualTo(charset);
    }

    static MapSettings getDefaultSettings() {
        return new MapSettings()
                .setProperty(RustLanguageSettings.FILE_SUFFIXES_KEY, ".foo");

    }
}
