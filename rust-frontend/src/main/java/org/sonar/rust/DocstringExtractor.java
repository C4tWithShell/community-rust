package org.sonar.rust;

import org.sonar.plugins.rust.api.tree.*;

import javax.annotation.Nullable;

public class DocstringExtractor {
    private DocstringExtractor() {
    }

    public static StringLiteral extractDocstring(@Nullable StatementList statements) {
        if (statements != null) {
            Statement firstStatement = statements.statements().get(0);
            if (firstStatement.is(Tree.Kind.EXPRESSION_STMT) && ((ExpressionStatement) firstStatement).expressions().size() == 1
                    && firstStatement.children().get(0).is(Tree.Kind.STRING_LITERAL)) {
                return (StringLiteral) firstStatement.children().get(0);
            }
        }
        return null;
    }
}
