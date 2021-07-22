package org.elegoff.rust.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

@Rule(key = "LineLength")
public class LineLengthCheck extends RustCheck {

    private static final int DEFAULT_MAXIMUM_LINE_LENHGTH = 120;

    @RuleProperty(
            key = "maximumLineLength",
            description = "The maximum authorized line length.",
            defaultValue = "" + DEFAULT_MAXIMUM_LINE_LENHGTH)
    public int maximumLineLength = DEFAULT_MAXIMUM_LINE_LENHGTH;

    @Override
    public void visitFile(AstNode astNode) {
        String[] lines = getContext().file().content().split("\\r?\\n");
        for (int i = 0; i < lines.length; i++) {
            int length = lines[i].length();
            if (length > maximumLineLength) {
                addLineIssue(
                        "Split this " + length + " characters long line (which is greater than " + maximumLineLength + " authorized).",
                        i + 1);
            }
        }
    }

}
