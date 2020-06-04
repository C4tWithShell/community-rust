package org.sonar.plugins.rust.api;

import org.sonar.plugins.rust.api.tree.Tree;

import java.io.File;

public interface SubscriptionContext {
    Tree syntaxNode();

    File workingDirectory();
}

