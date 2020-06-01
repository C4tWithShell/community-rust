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
package org.elegoff.plugins.rust.language;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RustLanguage)) {
            return false;
        }
        RustLanguage that = (RustLanguage) o;
        return KEY.equals(that.getKey());

    }

    @Override
    public int hashCode() {
        return KEY.hashCode();
    }

    @Override
    public String toString() {
        return NAME;
    }


}
