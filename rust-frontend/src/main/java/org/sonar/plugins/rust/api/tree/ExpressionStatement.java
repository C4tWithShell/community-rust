package org.sonar.plugins.rust.api.tree;

import java.util.List;


public interface ExpressionStatement extends Statement {
    List<Expression> expressions();
}