package org.sonar.rust.metrics;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.plugins.rust.api.RustSubscriptionCheck;
import org.sonar.plugins.rust.api.RustVisitorContext;
import org.sonar.plugins.rust.api.SubscriptionCheck;
import org.sonar.plugins.rust.api.SubscriptionContext;
import org.sonar.plugins.rust.api.tree.Token;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.SubscriptionVisitor;
import org.sonar.rust.api.RustTokenType;

import javax.annotation.Nullable;
import java.util.*;

public class FileLinesVisitor extends RustSubscriptionCheck {
    private Set<Integer> noSonar = new HashSet<>();
    private Set<Integer> linesOfCode = new HashSet<>();
    private Set<Integer> linesOfComments = new HashSet<>();
    private Set<Integer> linesOfDocstring = new HashSet<>();
    private Set<Integer> executableLines = new HashSet<>();
    private int statements = 0;
    private int classDefs = 0;

    private static final List<Tree.Kind> EXECUTABLE_LINES = Arrays.asList( Tree.Kind.EXPRESSION_STMT, Tree.Kind.FILE_INPUT);

    @Override
    public void scanFile(RustVisitorContext visitorContext) {
        SubscriptionVisitor.analyze(Collections.singleton(this), visitorContext);
    }

    @Override
    public void initialize(Context context) {
        context.registerSyntaxNodeConsumer(Tree.Kind.FILE_INPUT, ctx -> visitFile());
        EXECUTABLE_LINES.forEach(kind -> context.registerSyntaxNodeConsumer(kind, this::visitNode));
        context.registerSyntaxNodeConsumer(Tree.Kind.TOKEN, ctx -> visitToken((Token) ctx.syntaxNode()));
    }

    private void visitFile() {
        noSonar.clear();
        linesOfCode.clear();
        linesOfComments.clear();
        linesOfDocstring.clear();
        executableLines.clear();
    }
    private void visitNode(SubscriptionContext ctx) {
        Tree tree = ctx.syntaxNode();
        statements++;
        executableLines.add(tree.firstToken().line());
    }



    private void visitToken(Token token) {
        if (token.type().equals(GenericTokenType.EOF)) {
            return;
        }

        if (!token.type().equals(RustTokenType.NEWLINE)) {
            // Handle all the lines of the token
            String[] tokenLines = token.value().split("\n", -1);
            int tokenLine = token.line();
            for (int line = tokenLine; line < tokenLine + tokenLines.length; line++) {
                linesOfCode.add(line);
            }
        }

    }

    public Set<Integer> getLinesOfCode() {
        return Collections.unmodifiableSet(linesOfCode);
    }
}
