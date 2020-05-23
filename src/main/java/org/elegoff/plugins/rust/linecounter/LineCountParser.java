package org.elegoff.plugins.rust.linecounter;

import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * Counting comment lines, blank lines in RUST files
 */
public final class LineCountParser {
    private int linesNumber;
    private Set<Integer> commentLines;
    private Set<Integer> linesOfCodeLines;
    private LineCountData data;


    /**
     * Constructor. The passed string is parsed and an instance of {@code LineCountData} is created and ready for
     * retrieval with the method {@linkplain #getLineCountData()}
     *
     * @param contents the RUST content to be parsed
     * @see #getLineCountData()
     */
    public LineCountParser(String contents) {
        this.commentLines = new HashSet<>();
        this.linesOfCodeLines = new HashSet<>();
        linesNumber = 0;

        List<Parser.Line> lines = Parser.getLines(contents);

        for (Parser.Line line : lines) {
            String lineContent = line.getContent();
            if (isCommentLine(lineContent)) {
                commentLines.add(line.getLineNo());
            } else if (!StringUtils.isBlank(lineContent)) {
                linesOfCodeLines.add(line.getLineNo());
            }

            // Is it useful? Lists are ordered
            if (line.getLineNo() > linesNumber) {
                linesNumber = line.getLineNo();
            }
        }

        this.data = new LineCountData(
                linesNumber,
                linesOfCodeLines,
                commentLines);
    }

    /**
     * Returns the {@code LineCountData} describing the passed RUST document
     *
     * @return the {@code LineCountData} describing the passed RUST document
     */
    public LineCountData getLineCountData() {
        return data;
    }


    /**
     * Tells if the passed line is a comment line, i.e. a line with only a non-empty comment
     *
     * @param lineContent
     * @return {@code true} if the passed string represents a line of comment. Inline comments return {@code false}.
     */
    private boolean isCommentLine(String lineContent) {
        assert lineContent != null;

        return lineContent.trim().matches("^\\h*#\\h*\\S.*");
    }
}