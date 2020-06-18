package org.sonar.rust.ast.parser;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.typed.Optional;
import org.sonar.plugins.rust.api.tree.ExpressionTree;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.model.KindMaps;
import org.sonar.rust.model.RustTree;
import org.sonar.rust.model.expression.LiteralTreeImpl;

import java.util.List;

public class TreeFactory {

    private final KindMaps kindMaps = new KindMaps();

    public RustTree.CompilationUnitTreeImpl newCompilationUnit(
            InternalSyntaxToken token, InternalSyntaxToken any,
            InternalSyntaxToken eof) {



        return new RustTree.CompilationUnitTreeImpl(any, eof);
    }


    // Literals

    public ExpressionTree literal(InternalSyntaxToken token) {
        return new LiteralTreeImpl(kindMaps.getLiteral(token.getGrammarRuleKey()), token);
    }



    // End of literals
}
