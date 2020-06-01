package org.elegoff.plugins.rust.api.tree;

public interface Tree {
    Token firstToken();

    /**
     * @return the last meaningful token of the Tree.
     * Separators of simple statements (semicolon and/or newline) are not be returned by this method.
     */
    Token lastToken();
}
