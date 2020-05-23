package org.elegoff.plugins.rust.checks;

import org.sonar.check.Rule;

@Rule(key = "ParsingErrorCheck")
public class ParsingErrorCheck extends ClippyCheck {
    @Override
    public void validate() {
        // Do nothing, syntax errors are actually done in RustSensor
    }
}