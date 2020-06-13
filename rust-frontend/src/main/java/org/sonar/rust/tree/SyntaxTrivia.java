package org.sonar.rust.tree;

import org.sonar.plugins.rust.api.tree.Tree;

public interface SyntaxTrivia extends Tree {

    String comment();

    int startLine();

    int column();

}
