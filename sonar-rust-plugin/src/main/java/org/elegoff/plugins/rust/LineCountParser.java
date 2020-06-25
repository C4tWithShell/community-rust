package org.elegoff.plugins.rust;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineCountParser {
    private int linesNumber;
    private Set<Integer> commentLines;
    private Set<Integer> linesOfCodeLines;
    private LineCountData data;

    public LineCountParser(String contents) {
        this.commentLines = new HashSet<>();
        this.linesOfCodeLines = new HashSet<>();
        linesNumber = 0;

        List<Line> lines = getLines(contents);

        for (Line line : lines) {
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

    public LineCountData getLineCountData() {
        return data;
    }

    private boolean isCommentLine(String lineContent) {
        assert lineContent != null;

        //there should be more clever way to identify comments
        return lineContent.trim().matches("^\\.*");
    }

    private static List<Line> getLines(String buffer) {
        List<Line> lines = new ArrayList();
        int lineNo = 1;
        int cur = 0;

        for(int next = buffer.indexOf(10); next != -1; ++lineNo) {
            lines.add(new Line(lineNo, buffer, cur, next));
            cur = next + 1;
            next = buffer.indexOf(10, cur);
        }

        lines.add(new Line(lineNo, buffer, cur, buffer.length()));
        return lines;
    }
}
