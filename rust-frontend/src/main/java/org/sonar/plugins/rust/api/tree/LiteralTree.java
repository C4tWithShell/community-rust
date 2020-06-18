package org.sonar.plugins.rust.api.tree;

import org.sonar.rust.tree.SyntaxToken;



public interface LiteralTree extends ExpressionTree {

    String value();

    SyntaxToken token();
}