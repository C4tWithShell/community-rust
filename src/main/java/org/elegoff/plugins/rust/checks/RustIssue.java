package org.elegoff.plugins.rust.checks;

import org.sonar.api.rule.RuleKey;


public class RustIssue {
    /**
     * The {@code RuleKey} of the rule that identified the issue
     */
    protected final RuleKey ruleKey;

    /**
     * Flag that tells if this issue represents a syntax error or not
     */
    protected boolean syntaxError;

    /**
     * The line number at which the issue was found
     */
    protected int line;

    /**
     * The column at which the issue was found
     */
    protected int column;

    /**
     * A message describing the issue
     */
    protected String message;


    /**
     * Constructor for "standard" (non-syntactical issue)
     *
     * @param ruleKey the key of the rule that identified the issue
     * @param message a message describing the issue
     * @param line the line number at which the issue was found
     * @param column the column number at which the issue was found
     */
    public RustIssue(RuleKey ruleKey, String message, int line, int column) {
        this.ruleKey = ruleKey;
        this.syntaxError = false;
        this.message = message;
        this.line = line;
        this.column = column;
    }

    /**
     * Constructor for "standard" (non-syntactical issue)
     *
     * @param ruleKey the key of the rule that identified the issue
     * @param line the line number at which the issue was found
     * @param column the column number at which the issue was found
     * @param message a message describing the issue
     * @param syntaxError {@code true} if this issue corresponds to a syntax error, {@code false} otherwise
     */
    public RustIssue(RuleKey ruleKey, String message, int line, int column, boolean syntaxError) {
        this.ruleKey = ruleKey;
        this.message = message;
        this.line = line;
        this.column = column;
        this.syntaxError = syntaxError;
    }

    /**
     * Returns a message describing the issue
     *
     * @return a description of the issue
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the line number at which the issue was found
     *
     * @return the line number where the issue was found
     */
    public int getLine() {
        return line;
    }

    /**
     * Returns the column number at which the issue was found
     *
     * @return the column number where the issue was found
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the key of the rule associated with this issue
     *
     * @return the key of the rule associated with this issue
     */
    public RuleKey getRuleKey() {
        return ruleKey;
    }

    /**
     * Tells if this issue corresponds to a syntax error
     *
     * @return {@code true} if this issue relates to a syntax error, {@code false} otherwise
     */
    public boolean isSyntaxError() {
        return syntaxError;
    }
}