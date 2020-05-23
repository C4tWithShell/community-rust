package org.elegoff.plugins.rust.checks;

import org.elegoff.plugins.rust.clippy.LintProblem;
import org.sonar.api.rule.RuleKey;



public class ClippyIssue extends RustIssue {
    /**
     * Constructor for "standard" (non-syntactical issue)
     *
     * @param problem the source {@link LintProblem}
     * @param ruleKey the key of the rule that identified the issue
     */
    public ClippyIssue(LintProblem problem, RuleKey ruleKey) {
        this(problem, ruleKey, false);
    }

    /**
     * Constructor
     *
     * @param problem the source {@link LintProblem}
     * @param ruleKey the key of the rule that identified the issue
     * @param syntaxError {@code true} if this issue corresponds to a syntax error, {@code false} if not
     */
    public ClippyIssue(LintProblem problem, RuleKey ruleKey, boolean syntaxError) {
        super(ruleKey, problem.getMessage(), problem.getLine(), problem.getColumn(), syntaxError);
    }
}