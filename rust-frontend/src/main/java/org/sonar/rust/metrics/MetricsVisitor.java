package org.sonar.rust.metrics;

import com.google.common.collect.ImmutableSet;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustLexer;
import org.sonar.rust.RustParserConfiguration;
import org.sonar.rust.RustVisitorContext;

import java.util.Set;

public class MetricsVisitor {

    private final LinesOfCodeVisitor linesOfCodeVisitor;
    private final CommentsVisitor commentsVisitor;
    private final ComplexityVisitor complexityVisitor;
    private int numberOfStatements;
    private int numberOfFunctions;

    public MetricsVisitor(RustParserConfiguration conf) {
        linesOfCodeVisitor = new LinesOfCodeVisitor(RustLexer.create(conf));
        commentsVisitor = new CommentsVisitor();
        complexityVisitor = new ComplexityVisitor();
    }

    public void scanFile(RustVisitorContext context) {
        linesOfCodeVisitor.scanFile(context);
        commentsVisitor.scanFile(context);
        complexityVisitor.scanFile(context);
        numberOfStatements = context.rootTree().getDescendants(RustGrammar.STATEMENT).size();
        numberOfFunctions = context.rootTree().getDescendants(RustGrammar.FUNCTION).size();
    }

    public Set<Integer> linesOfCode() {
        return linesOfCodeVisitor.linesOfCode();
    }

    public Set<Integer> noSonarTagLines() {
        return ImmutableSet.of();
    }

    public Set<Integer> commentLines() {
        return commentsVisitor.commentLines();
    }

    public int numberOfStatements() {
        return numberOfStatements;
    }

    public int numberOfFunctions() {
        return numberOfFunctions;
    }

    public int complexity() {
        return complexityVisitor.complexity();
    }

}
