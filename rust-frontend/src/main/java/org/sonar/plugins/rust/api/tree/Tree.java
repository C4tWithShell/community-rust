package org.sonar.plugins.rust.api.tree;

import java.util.List;

public interface Tree {

    void accept(TreeVisitor visitor);

    boolean is(Kind... kinds);

    Token firstToken();

    /**
     * @return the last meaningful token of the Tree.
     * Separators of simple statements (semicolon and/or newline) are not be returned by this method.
     */
    Token lastToken();

    Tree parent();

    List<Tree> children();

    enum Kind {
        EXPRESSION_STMT(ExpressionStatement.class),
       FILE_INPUT(FileInput.class),
        STATEMENT_LIST(StatementList.class),
        STRING_LITERAL(StringLiteral.class),
        TOKEN(Token.class);
        final Class<? extends Tree> associatedInterface;
        Kind(Class<? extends Tree> associatedInterface) {
            this.associatedInterface = associatedInterface;
        }
    }

    Kind getKind();
}
