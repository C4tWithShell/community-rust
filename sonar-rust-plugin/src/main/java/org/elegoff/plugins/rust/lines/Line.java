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

public class Line {
    private int lineNo;
    private int start;
    private int end;
    private String buffer;

    public Line(int lineNo, String buffer, int start, int end) {
        this.lineNo = lineNo;
        this.start = start;
        this.end = end;
        this.buffer = buffer;
    }

    public String getContent() {
        return this.buffer.substring(this.start, this.end);
    }

    public int getLineNo() {
        return this.lineNo;
    }

    public int getEnd() {
        return this.end;
    }

    public int getStart() {
        return this.start;
    }

    public String getBuffer() {
        return this.buffer;
    }
}
