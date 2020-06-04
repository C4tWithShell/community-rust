package org.sonar.rust.tree;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import org.sonar.plugins.rust.api.tree.FileInput;
import org.sonar.plugins.rust.api.tree.Statement;
import org.sonar.plugins.rust.api.tree.Token;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.DocstringExtractor;
import org.sonar.rust.api.RustGrammar;
import org.sonar.rust.api.RustTokenType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RustTreeMaker {

    public FileInput fileInput(AstNode astNode) {
        List<Statement> statements = getStatements(astNode).stream().map(this::statement).collect(Collectors.toList());
        StatementListImpl statementList = statements.isEmpty() ? null : new StatementListImpl(statements);
        Token endOfFile = toRsToken(astNode.getFirstChild(GenericTokenType.EOF).getToken());
        FileInputImpl rsFileInputTree = new FileInputImpl(statementList, endOfFile, DocstringExtractor.extractDocstring(statementList));
        setParents(rsFileInputTree);
        return rsFileInputTree;
    }

    private static List<StatementWithSeparator> getStatements(AstNode astNode) {
        List<AstNode> statements = astNode.getChildren(RustGrammar.EXPRESSION);
        List<StatementWithSeparator> statementsWithSeparators = new ArrayList<>();
/*
        for (AstNode stmt : statements) {
            if (stmt.hasDirectChildren(RustGrammar.STMT_LIST)) {
                List<StatementWithSeparator> statementList = getStatementsWithSeparators(stmt);
                statementsWithSeparators.addAll(statementList);
            } else {
                StatementWithSeparator compoundStmt = new StatementWithSeparator(stmt.getFirstChild(RustGrammar.COMPOUND_STMT).getFirstChild(), null);
                statementsWithSeparators.add(compoundStmt);
            }
        }
   */
        return statementsWithSeparators;
    }

    private static Token toRsToken(@Nullable com.sonar.sslr.api.Token token) {
        if (token == null) {
            return null;
        }
        return new TokenImpl(token);
    }

    private Statement statement(StatementWithSeparator statementWithSeparator) {
        AstNode astNode = statementWithSeparator.statement();
        /* kept as an example of condtion on statement
        if (astNode.is(PythonGrammar.IF_STMT)) {
            return ifStatement(astNode);
        }
       */
        throw new IllegalStateException("Statement " + astNode.getType() + " not correctly translated to strongly typed AST");
    }
    public void setParents(Tree root) {
        for (Tree child : root.children()) {
            if (child != null) {
                ((RsTree) child).setParent(root);
                setParents(child);
            }
        }
    }


}
