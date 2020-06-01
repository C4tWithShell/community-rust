package org.elegoff.plugins.rust.api;

public class LocationInFile {
    private final String fileId;
    private final int startLine;
    private final int startLineOffset;
    private final int endLine;
    private final int endLineOffset;

    public LocationInFile(String fileId, int startLine, int startLineOffset, int endLine, int endLineOffset) {
        this.fileId = fileId;
        this.startLine = startLine;
        this.startLineOffset = startLineOffset;
        this.endLine = endLine;
        this.endLineOffset = endLineOffset;
    }

    public String fileId() {
        return fileId;
    }

    public int startLine() {
        return startLine;
    }

    public int startLineOffset() {
        return startLineOffset;
    }

    public int endLine() {
        return endLine;
    }

    public int endLineOffset() {
        return endLineOffset;
    }
}
