package org.sonar.plugins.rust.api.tree;

import org.sonar.rust.tree.SyntaxToken;


public interface CompilationUnitTree extends Tree{

    SyntaxToken eofToken();

}
