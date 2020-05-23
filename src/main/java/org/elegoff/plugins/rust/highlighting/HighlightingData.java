package org.elegoff.plugins.rust.highlighting;

import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;

/**
 * Class that stores the parameters a portion of RUST code to be highlighted and that is responsible for
 * highlighting code in Sonar
 */
public class HighlightingData {
    private final TypeOfText typeOfText;

    private int startLine;
    private int startColumnOffset;
    private int endLine;
    private int endColumnOffset;

    /**
     * Constructor
     *
     * @param startLine the line number where the highlighted portion of code starts
     * @param startColumnIndex the column number where the highlighted portion of code starts
     * @param endLine the line number where the highlighted portion of code ends
     * @param endColumnIndex the column number where the highlighted portion of code ends (this column is not part of
     *                       the highlighting)
     * @param typeOfText the type of text. Depending on this parameter, the highlighting in SonarQube will be different.
     */
    public HighlightingData(int startLine, int startColumnIndex, int endLine, int endColumnIndex, TypeOfText typeOfText) {
        this.startLine = startLine;
        this.startColumnOffset = startColumnIndex - 1;
        this.endLine = endLine;
        this.endColumnOffset = endColumnIndex - 1;
        this.typeOfText = typeOfText;
    }

    /**
     * Highlights the portion of code in SonarQube as described by this class
     *
     * @param highlighting SonarQube's highlighting for this portion of code
     */
    public void highlight(NewHighlighting highlighting) {
        highlighting.highlight(startLine, startColumnOffset, endLine, endColumnOffset, typeOfText);
    }

    /**
     * Returns the line number where the highlighted portion of code starts
     *
     * @return the line number where the highlighted portion of code starts
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * Returns the column number where the highlighted portion of code starts
     *
     * @return the column number where the highlighted portion of code starts
     */
    public int getStartColumnIndex() {
        return startColumnOffset + 1;
    }

    /**
     * Returns the line number where the highlighted portion of code ends
     *
     * @return the line number where the highlighted portion of code ends
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * Returns the column number where the highlighted portion of code ends
     *
     * @return the column number where the highlighted portion of code ends
     */
    public int getEndColumnIndex() {
        return endColumnOffset + 1;
    }

    /**
     * Returns the type of text
     *
     * @return the type of text
     */
    public TypeOfText getTypeOfText() {
        return typeOfText;
    }
}