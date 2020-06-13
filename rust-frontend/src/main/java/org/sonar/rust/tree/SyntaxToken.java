package org.sonar.rust.tree;

import org.sonar.plugins.rust.api.tree.Tree;

import java.util.List;

public interface SyntaxToken extends Tree {

    String text();

    List<SyntaxTrivia> trivias();

    int line();

    int column();
}
