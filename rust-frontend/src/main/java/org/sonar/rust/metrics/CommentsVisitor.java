package org.sonar.rust.metrics;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.rust.RustVisitor;

import java.util.HashSet;
import java.util.Set;

public class CommentsVisitor extends RustVisitor {



    private Set<Integer> comments;
    private boolean seenFirstToken;


    public Set<Integer> commentLines() {
        return comments;
    }

    private void addCommentLine(int line) {
        comments.add(line);
    }

    @Override
    public void visitFile(AstNode astNode) {
        comments = new HashSet<>();
        seenFirstToken = false;
    }

    @Override
    public void visitToken(Token token) {
        if (seenFirstToken) {
            for (Trivia trivia : token.getTrivia()) {
                if (trivia.isComment()) {
                    String[] commentLines = getContents(trivia.getToken().getOriginalValue())
                            .split("(\r)?\n|\r", -1);
                    int line = trivia.getToken().getLine();

                    for (String commentLine : commentLines) {
                        if (!isBlank(commentLine)) {
                            addCommentLine(line);
                        }

                        line++;
                    }
                }
            }
        }

        seenFirstToken = true;
    }

    public boolean isBlank(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetterOrDigit(line.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public String getContents(String comment) {
        return comment.substring(2, comment.length() - 2);
    }


}
