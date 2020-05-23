package org.elegoff.plugins.rust.checks;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = "BracesCheck")
public class BracesCheck extends ClippyCheck {
    @RuleProperty(key = "min-spaces-inside", description = "Minimal number of spaces required inside braces", defaultValue = "0")
    int minSpacesInside;

    @RuleProperty(key = "max-spaces-inside", description = "Maximal number of spaces required inside braces", defaultValue = "0")
    int maxSpacesInside;

    @RuleProperty(key = "min-spaces-inside-empty", description = "Minimal number of spaces required inside empty braces", defaultValue = "-1")
    int minSpacesInsideEmpty;

    @RuleProperty(key = "max-spaces-inside-empty", description = "Maximal number of spaces required inside empty braces", defaultValue = "-1")
    int maxSpacesInsideEmpty;
}