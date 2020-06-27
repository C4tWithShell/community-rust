package org.elegoff.rust.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import org.sonar.api.internal.google.common.collect.ImmutableList;
import org.sonar.rust.RustVisitor;
import org.sonar.rust.RustVisitorContext;

import java.util.ArrayList;
import java.util.List;

public class RustCheck extends RustVisitor {
    private List<Issue> issues = new ArrayList<>();

    public List<Issue> scanFileForIssues(RustVisitorContext context) {
        issues.clear();
        scanFile(context);
        return ImmutableList.copyOf(issues);
    }

    public void addIssue(String message, AstNode node) {
        addIssue(message, node.getToken());
    }

    public void addIssue(String message, Token token) {
        if (token.getURI().equals(getContext().file().uri())) {
            addLineIssue(message, token.getLine());
        }
    }

    public void addLineIssue(String message, int line) {
        issues.add(Issue.lineIssue(this, line, message));
    }

    public void addFileIssue(String message) {
        issues.add(Issue.fileIssue(this, message));
    }
}
