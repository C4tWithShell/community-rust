package org.sonar.plugins.rust.api.tree;

import java.util.List;


public interface StatementList extends Tree {
    List<Statement> statements();
}
