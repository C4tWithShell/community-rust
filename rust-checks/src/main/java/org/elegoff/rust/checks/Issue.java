package org.elegoff.rust.checks;

import javax.annotation.CheckForNull;

public class Issue {

    private final RustCheck check;
    private final Integer line;
    private final String message;

    private Issue(RustCheck check, Integer line, String message) {
        this.check = check;
        this.line = line;
        this.message = message;
    }

    public static Issue fileIssue(RustCheck check, String message) {
        return new Issue(check, null, message);
    }

    public static Issue lineIssue(RustCheck check, int line, String message) {
        return new Issue(check, line, message);
    }

    public RustCheck check() {
        return check;
    }

    @CheckForNull
    public Integer line() {
        return line;
    }

    public String message() {
        return message;
    }
}
