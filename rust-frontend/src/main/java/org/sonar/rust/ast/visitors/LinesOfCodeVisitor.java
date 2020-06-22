package org.sonar.rust.ast.visitors;

import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.SubscriptionVisitor;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.tree.SyntaxToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LinesOfCodeVisitor extends SubscriptionVisitor {

    private Set<Integer> lines = new HashSet<>();

    public int linesOfCode(Tree tree) {
        lines.clear();
        scanTree(tree);
        return lines.size();
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Collections.singletonList(Tree.Kind.TOKEN);
    }

    @Override
    public void visitToken(SyntaxToken syntaxToken) {
        if (!((InternalSyntaxToken) syntaxToken).isEOF()) {
            lines.add(syntaxToken.line());
        }
    }
}
