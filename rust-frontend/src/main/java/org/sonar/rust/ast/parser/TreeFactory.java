package org.sonar.rust.ast.parser;

import org.sonar.plugins.rust.api.tree.ExpressionTree;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.model.KindMaps;
import org.sonar.rust.model.RustTree;
import org.sonar.rust.model.expression.LiteralTreeImpl;


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
