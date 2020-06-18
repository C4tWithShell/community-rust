package org.sonar.rust.ast.parser;

import com.sonar.sslr.api.typed.GrammarBuilder;
import org.sonar.plugins.rust.api.tree.ExpressionTree;
import org.sonar.rust.model.InternalSyntaxToken;
import org.sonar.rust.model.RustTree;

public class RustGrammar {
    private final GrammarBuilder<InternalSyntaxToken> b;
    private final TreeFactory f;

    public RustGrammar(GrammarBuilder<InternalSyntaxToken> b, TreeFactory f) {
        this.b = b;
        this.f = f;
    }


    public RustTree.CompilationUnitTreeImpl COMPILATION_UNIT() {
        return b.<RustTree.CompilationUnitTreeImpl>nonterminal(org.sonar.rust.ast.parser.RustLexer.COMPILATION_UNIT)
                .is(
                        f.newCompilationUnit(
                               b.token(RustLexer.SPACING),

                                b.token(RustLexer.EXPRESSION),
                                b.token(org.sonar.rust.ast.parser.RustLexer.EOF)));
    }







}
