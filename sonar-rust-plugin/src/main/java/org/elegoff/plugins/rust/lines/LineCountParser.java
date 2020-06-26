/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.rust.lines;

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
        List<Line> lines = new ArrayList<Line>();
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
