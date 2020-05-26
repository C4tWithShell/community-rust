package org.elegoff.plugins.rust.languages;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;


/**
 * Default, BuiltIn Quality Profile for the projects having files of the language "rust"
 */
public final class RustQualityProfile implements BuiltInQualityProfilesDefinition {

    @Override
    public void define(Context context) {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile("RUST Rules", RustLanguage.KEY);
        profile.setDefault(true);
        profile.done();
    }
}