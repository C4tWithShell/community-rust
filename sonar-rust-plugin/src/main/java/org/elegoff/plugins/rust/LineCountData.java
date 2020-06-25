package org.elegoff.plugins.rust;

import java.util.Collection;
import java.util.Set;

public class LineCountData {
    private Integer linesNumber;
    private Set<Integer> linesOfCodeLines;
    private Set<Integer> effectiveCommentLine;

    public LineCountData(Integer linesNumber, Set<Integer> linesOfCodeLines, Set<Integer> effectiveCommentLine) {
        this.linesNumber = linesNumber;
        this.linesOfCodeLines = linesOfCodeLines;
        this.effectiveCommentLine = effectiveCommentLine;
    }

    public Integer linesNumber() {
        return linesNumber;
    }

    public Set<Integer> linesOfCodeLines() {
        return linesOfCodeLines;
    }

    public Set<Integer> effectiveCommentLines() {
        return effectiveCommentLine;
    }
}
