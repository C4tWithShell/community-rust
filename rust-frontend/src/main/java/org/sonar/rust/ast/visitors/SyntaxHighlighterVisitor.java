package org.sonar.rust.ast.visitors;

import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.SubscriptionVisitor;

import java.util.List;

public class SyntaxHighlighterVisitor extends SubscriptionVisitor {
    public SyntaxHighlighterVisitor(SonarComponents sonarComponents) {
        //TODO
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        //TODO
        return null;
    }
}
