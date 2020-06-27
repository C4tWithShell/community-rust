package org.sonar.rust;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;

import java.util.List;
import java.util.Set;

public class RustVisitor {

    private RustVisitorContext context;

    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of();
    }

    public void visitFile(AstNode node) {
    }

    public void leaveFile(AstNode node) {
    }

    public void visitNode(AstNode node) {
    }

    public void visitToken(Token token) {
    }

    public void leaveNode(AstNode node) {
    }

    public RustVisitorContext getContext() {
        return context;
    }

    public void scanFile(RustVisitorContext context) {
        this.context = context;
        AstNode tree = context.rootTree();
        visitFile(tree);
        if (tree != null) {
            scanNode(tree, subscribedKinds());
        }
        leaveFile(tree);
    }

    private void scanNode(AstNode node, Set<AstNodeType> subscribedKinds) {
        boolean isSubscribedType = subscribedKinds.contains(node.getType());

        if (isSubscribedType) {
            visitNode(node);
        }

        List<AstNode> children = node.getChildren();
        if (children.isEmpty()) {
            for (Token token : node.getTokens()) {
                visitToken(token);
            }
        } else {
            for (AstNode child : children) {
                scanNode(child, subscribedKinds);
            }
        }

        if (isSubscribedType) {
            leaveNode(node);
        }
    }

}
