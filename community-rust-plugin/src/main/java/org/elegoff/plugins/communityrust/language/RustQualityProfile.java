/*
 * Community Rust Plugin
 * Copyright (C) 2021-2025 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
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
package org.elegoff.plugins.communityrust.language;

import org.elegoff.plugins.communityrust.rules.RustRulesDefinition;
import org.elegoff.rust.checks.CheckList;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "rust"
 */
public final class RustQualityProfile implements BuiltInQualityProfilesDefinition {

  public static final String PROFILE_PATH = RustRulesDefinition.RESOURCE_BASE_PATH + "/built-in-profile.json";
  static final String PROFILE_NAME = "Community Rust";

  @Override
  public void define(Context context) {
    NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(PROFILE_NAME, RustLanguage.KEY);
    BuiltInQualityProfileJsonLoader.load(profile, CheckList.REPOSITORY_KEY, PROFILE_PATH);
    profile.setDefault(true);
    profile.done();
  }
}
