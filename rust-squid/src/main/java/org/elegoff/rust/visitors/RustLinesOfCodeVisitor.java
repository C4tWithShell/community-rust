package org.elegoff.rust.visitors;

import com.sonar.sslr.api.*;
import org.elegoff.rust.api.RustMetric;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;

import java.util.regex.Pattern;

import static com.sonar.sslr.api.GenericTokenType.EOF;

public class RustLinesOfCodeVisitor<GRAMMAR extends Grammar>
        extends SquidAstVisitor<GRAMMAR> implements AstAndTokenVisitor {

    public static final Pattern EOL_PATTERN = Pattern.compile("\\R");

    private int lastTokenLine;

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitFile(AstNode node) {
        lastTokenLine = -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitToken(Token token) {
        if (token.getType().equals(EOF)) {
            return;
        }

        // handle all the lines of the token
        String[] tokenLines = EOL_PATTERN.split(token.getValue(), -1);

        int firstLineAlreadyCounted = lastTokenLine == token.getLine() ? 1 : 0;
        getContext().peekSourceCode().add(RustMetric.LINES_OF_CODE, (double) tokenLines.length - firstLineAlreadyCounted);

        lastTokenLine = token.getLine() + tokenLines.length - 1;

        // handle comments
        for (var trivia : token.getTrivia()) {
            if (trivia.isComment()) {
                visitComment(trivia);
            }
        }
    }

    /**
     * Search in comments for NOSONAR
     */
    public void visitComment(Trivia trivia) {
        String[] commentLines = EOL_PATTERN
                .split(getContext().getCommentAnalyser().getContents(trivia.getToken().getOriginalValue()), -1);
        int line = trivia.getToken().getLine();

        for (var commentLine : commentLines) {
            if (commentLine.contains("NOSONAR")) {
                SourceCode sourceCode = getContext().peekSourceCode();
                if (sourceCode instanceof SourceFile) {
                    ((SourceFile) sourceCode).hasNoSonarTagAtLine(line);
                }
            }
            line++;
        }
    }

}

