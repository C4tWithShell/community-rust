package org.sonar.rust.metrics;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustVisitor;

import java.util.List;
import java.util.Set;

public class ComplexityVisitor extends RustVisitor{

    private int complexity;

    public int complexity() {
        return complexity;
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.<AstNodeType>builder()
                .add(RustGrammar.EXPRESSION_STATEMENT)
                .build();
    }

    @Override
    public void visitFile(AstNode astNode) {
        complexity = 0;
    }

    @Override
    public void visitNode(AstNode astNode) {
        if (!isSomethingComplex(astNode)) {
            complexity++;
        }
    }

    private static boolean isSomethingComplex(AstNode node) {
        //placeholder for ideas may have ideas later
        return false;
    }



}
