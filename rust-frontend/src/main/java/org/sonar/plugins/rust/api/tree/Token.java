package org.sonar.plugins.rust.api.tree;

import com.sonar.sslr.api.TokenType;


public interface Token extends Tree {
    String value();

    int line();

    int column();

    TokenType type();
}
