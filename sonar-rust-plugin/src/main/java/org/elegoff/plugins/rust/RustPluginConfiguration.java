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
