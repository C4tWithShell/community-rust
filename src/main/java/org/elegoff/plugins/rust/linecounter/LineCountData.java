package org.elegoff.plugins.rust.linecounter;

import java.util.Set;

/**
 * Bean that holds a summary of the lines of a RUST file (total number of lines, actual line numbers of line of code and
 * line numbers of comments)
 */
public class LineCountData {
    private Integer linesNumber;
    private Set<Integer> linesOfCodeLines;
    private Set<Integer> effectiveCommentLine;

    /**
     * Constructor
     *
     * @param linesNumber a number of lines
     * @param linesOfCodeLines set of line numbers of line of code
     * @param effectiveCommentLine set of line numbers of comments
     */
    public LineCountData(Integer linesNumber, Set<Integer> linesOfCodeLines, Set<Integer> effectiveCommentLine) {
        this.linesNumber = linesNumber;
        this.linesOfCodeLines = linesOfCodeLines;
        this.effectiveCommentLine = effectiveCommentLine;
    }

    /**
     * Returns the total number of line of a RUST file
     *
     * @return the total number of line of a RUST file
     */
    public Integer linesNumber() {
        return linesNumber;
    }

    /**
     * Returns the set of line numbers of lines of code
     *
     * @return the set of line numbers of lines of code
     */
    public Set<Integer> linesOfCodeLines() {
        return linesOfCodeLines;
    }

    /**
     * Returns the set of line numbers of comments
     *
     * @return the set of line numbers of comments
     */
    public Set<Integer> effectiveCommentLines() {
        return effectiveCommentLine;
    }
}