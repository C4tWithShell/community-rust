package org.elegoff.plugins.rust.checks;

import org.sonar.api.rule.RuleKey;

/**
 * Abstract class that all RUST checks should extend
 */
public abstract class RustCheck {
    /**
     * The {@code RuleKey} of this check
     */
    protected RuleKey ruleKey = null;

    /**
     * The {@code RustSourceCode} instance this check has analyzed or will analyze
     */
    protected RustSourceCode rustSourceCode = null;


    /**
     * Sets the {@code RuleKey} of this check
     *
     * @param ruleKey the {@code RuleKey} of this check
     */
    public final void setRuleKey(RuleKey ruleKey) {
        this.ruleKey = ruleKey;
    }

    /**
     * Returns the {@code RuleKey} of this check
     *
     * @return the {@code RuleKey} of this check, possibly {@code null}
     */
    public RuleKey getRuleKey() {
        return ruleKey;
    }

    /**
     * Sets the source code on which the check is to be performed
     * <p><strong>Call this method before calling {@link #validate()}!</strong></p>
     *
     * @param rustSourceCode the source code to be checked
     */
    public void setRustSourceCode(RustSourceCode rustSourceCode) {
        this.rustSourceCode = rustSourceCode;
    }

    /**
     * Returns the {@code RustSourceCode} instance this check has analyzed or will analyze
     *
     * @return the {@code RustSourceCode} instance this check has analyzed or will analyze
     */
    public RustSourceCode getRustSourceCode() {
        return rustSourceCode;
    }

    /**
     * Validates a source code, creating violations for each error found.
     * <p>The default implementation executes the Clippy rule whose name corresponds to the check class name
     * (minus the suffix {@literal "Check"}.</p>
     *
     * @throws IllegalStateException if there is no source code to validate, i.e. if {@link #setRustSourceCode(RustSourceCode)} has not been called first
     */
    public abstract void validate();
}