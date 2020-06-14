package org.sonar.plugins.rust.api.tree;

import org.sonar.rust.tree.SyntaxToken;

import javax.annotation.Nullable;
import java.util.List;

public interface CompilationUnitTree extends Tree{
    List<Tree> types();

    SyntaxToken eofToken();

}
