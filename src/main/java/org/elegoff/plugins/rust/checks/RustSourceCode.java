package org.elegoff.plugins.rust.checks;

import org.elegoff.plugins.rust.clippy.LintProblem;
import org.elegoff.plugins.rust.clippy.Linter;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Wrapper class to ease the working with files and associated issues
 */
public class RustSourceCode {
    private static final Logger LOGGER = Loggers.get(RustSourceCode.class);


    private final List<RustIssue> rustIssues = new ArrayList<>();

    private RustIssue syntaxError = null;
    private boolean filter;
    private InputFile rustFile;
    private String content = null;


    /**
     * Constructor. Parses the passed file to determine if it is syntactically correct.
     *
     * @param rustFile a supposedly Rust file
     * @param filter {@code true} to filter out UTF-8 line break characters (U+2028, U+2029 and U+0085) that may not be
     *               correctly supported by SonarQube
     * @throws IOException if there is a problem reading the passed file
     */
    public RustSourceCode(InputFile rustFile, Optional<Boolean> filter) throws IOException {
        this.rustFile = rustFile;
        this.filter = filter.isPresent()?filter.get():false;

        LintProblem problem = Linter.getSyntaxError(getContent());
        LOGGER.debug("File {} has syntax error? {}", rustFile.uri(), problem != null);
        
       
    }


    /**
     * Returns the {@code InputFile} of this class.
     * <p><strong>WARNING!!!</strong> Do not use {@code getRustFile.contents()} to get the source; use {@link #getContent()}
     * instead.</p>
     *
     * @return the {@code InputFile} of this class
     * @see InputFile
     * @see #getContent()
     */
    public InputFile getRustFile() {
        return rustFile;
    }

    /**
     * Returns the content of the RUST file as a {@code String} with UTF-8 line breaks possibly removed.
     * <p><strong>WARNING!!</strong> Use this method instead of {@code InputFile.contents()} in order to have the source
     * code to be cleanup if needed.</p>
     *
     * @return the RUST content
     * @throws IOException if an error occurred reading the RUST file
     * @see #RustSourceCode(InputFile, Optional)
     */
    public String getContent() throws IOException {
        if (content == null) {
            if (filter) {
                this.content = rustFile.contents().replaceAll("\u0085", "").replaceAll("\u2028", "").replaceAll("\u2029", "");
            } else {
                this.content = rustFile.contents();
            }
        }

        return content;
    }

    /**
     * Adds an issue to list of issues already discovered
     *
     * @param issue an issue that relates to this RUST source code
     */
    public void addViolation(RustIssue issue) {
        this.rustIssues.add(issue);
        if (issue.isSyntaxError() && syntaxError == null) {
            syntaxError = issue;
        }
    }

    /**
     * Returns the syntax error if any found. May be {@code null}.
     * <p><strong>Warning!!!</strong> This issue has no rule key. It is up to the caller of this method to deal with the
     * rule key if it is required.</p>
     *
     * @return the syntax error if any
     * @see #hasCorrectSyntax()
     */
    public RustIssue getSyntaxError() {
        return syntaxError;
    }
    /**
     * Returns {@code true} if succeeded or {@code false} if the file is corrupted (i.e. it contains
     * a syntax error you can get with {@link #getSyntaxError()})
     *
     * @return {@code true} if succeeded or {@code false} if the file is corrupted
     */
    public boolean hasCorrectSyntax() {
        return syntaxError == null;
    }

    /**
     * Returns the issues found on the source code
     *
     * @return the list of issues found on the source code. The returned list may be empty, whether because no issue has
     * been found, or the source code has not been parsed yet
     */
    public List<RustIssue> getRustIssues() {
        return rustIssues;
    }
}