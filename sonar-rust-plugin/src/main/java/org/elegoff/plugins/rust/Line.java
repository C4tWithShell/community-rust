package org.elegoff.plugins.rust;

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
