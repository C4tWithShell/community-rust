package org.elegoff.plugins.rust.checks;

import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.RuleProperty;

import org.elegoff.plugins.rust.clippy.LintProblem;
import org.elegoff.plugins.rust.clippy.Linter;
import org.elegoff.plugins.rust.clippy.ClippyConfig;
import org.elegoff.plugins.rust.clippy.ClippyConfigException;


import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Abstract class for all RUST checks representing a Clippy rule
 */
public abstract class ClippyCheck extends RustCheck {
    private static final Logger LOGGER = Loggers.get(ClippyCheck.class);


    @Override
    public void validate() {
        if (rustSourceCode == null) {
            throw new IllegalStateException("Source code not set, cannot validate anything");
        }

        
        try {
            List<LintProblem> problems = Linter.getCosmeticProblems(getRustSourceCode().getContent(), getClippyConfig(), null);
            LOGGER.debug("Problems found: " + problems);
            for (LintProblem problem : problems) {
                LOGGER.debug("Creating violation for " + problem);
                createViolation(problem);
            }
        } catch (ClippyConfigException e) {
            LOGGER.warn("Cannot get ClippyConfig for rule '" + getClippyRuleID() + "'", e);

        } catch (IOException e) {
            // Should not happen: a first call to getRustSourceCode().getContent() was done in the constructor of
            // the RustSourceCode instance of this check, but in case...
            LOGGER.warn("Cannot read source code", e);
        }
        
    }


    /**
     * Registers a violation that is no a syntax error
     *
     * @param violation a problem representing the violation
     */
    protected final void createViolation(LintProblem violation) {
        getRustSourceCode().addViolation(new ClippyIssue(violation, getRuleKey()));
    }

    /**
     * Registers a violation that may be said to be a syntax error
     *
     * @param violation a problem representing the violation
     * @param syntaxError {@code true} if the violation must be declared as a syntax error, {@code false} if not (this
     *                    is an "ordinary" violation)
     */
    protected final void createViolation(LintProblem violation, boolean syntaxError) {
        getRustSourceCode().addViolation(new ClippyIssue(violation, getRuleKey(), syntaxError));
    }

    /**
     * Tells with Ant style {@code filepattern} if the file analyzed being is included. / are always used as the
     * file separator
     *
     * @param filePattern an Ant style file pattern ({@code **\/*.rs}, etc.)
     * @return {@code true} is the path of the file source code being checked matches the passed pattern,
     * {@code false} otherwise
     */
    protected boolean isFileIncluded(@Nullable String filePattern) {
        if (filePattern != null) {
            return WildcardPattern.create(filePattern)
                    .match(getRustSourceCode().getRustFile().uri().getPath());

        } else {
            return true;
        }
    }

    /**
     * Returns the Clippy rule  ID of the rule corresponding to this check. The rule ID is calculated from the class name as follows:
     * <ul>
     *     <li>The suffix "Check" is removed from the class name</li>
     *     <li>An hyphen ("-") is inserted before every capital letter of the class name (except for the first letter)</li>
     *     <li>All lowercase</li>
     * </ul>
     * <p>Example: if the class name is {@code FooBarCheck} then the Clippy ID returned by this method will be {@code "foo-bar"}</p>
     *
     * @return a string that is a Clippy rule ID
     */
    protected String getClippyRuleID() {
        return this.getClass().getName().replaceAll(".*\\.", "").replaceAll("Check$", "").replaceAll("([A-Z])", "-$1").substring(1).toLowerCase();
    }

    /**
     * Returns an instance of {@code ClippyConfig} that corresponds to the configuration of the current rule
     *
     * @return an instance of {@code ClippyConfig}
     * @throws ClippyConfigException if an error occurred building the instance of {@code ClippyConfig}
     * @see ClippyConfig
     */
    protected ClippyConfig getClippyConfig() throws ClippyConfigException {
        StringBuilder propsSB = new StringBuilder();
        for (Field f : getClass().getDeclaredFields()) {
            RuleProperty rp = f.getAnnotation(RuleProperty.class);
            LOGGER.debug("Got RuleProperty " + rp);
            if (rp != null) {
                try {
                    propsSB.append("    ").append(rp.key()).append(": ").append(f.get(this)).append("\n");
                } catch (IllegalAccessException e) {
                    LOGGER.warn("Cannot get field value for '" + f.getName() + "'", e);
                    return null;
                }
            }
        }

        StringBuilder confSB = new StringBuilder("---\n").append("rules:\n").append("  ").append(getClippyRuleID()).append(":");
        if (propsSB.length() == 0) {
            confSB.append(" enable");
        } else {
            confSB.append("\n").append(propsSB);
        }

        LOGGER.debug("Clippy config for rule " + getRuleKey() + "/" + getClippyRuleID() + ": '" + confSB.toString() + "'");
        return new ClippyConfig(confSB.toString());
    }
}