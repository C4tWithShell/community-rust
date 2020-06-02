package org.elegoff.rust;


import com.sonar.sslr.api.Grammar;
import org.elegoff.rust.api.RustMetric;
import org.elegoff.rust.parser.RustGrammarImpl;
import org.elegoff.rust.parser.RustParser;
import org.elegoff.rust.visitors.RustFileLinesVisitor;
import org.elegoff.rust.visitors.RustFileVisitor;
import org.elegoff.rust.visitors.RustLinesOfCodeVisitor;
import org.sonar.squidbridge.*;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.ComplexityVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;

public class RustAstScanner {

    private RustAstScanner(){

    }

    @SafeVarargs
    public static AstScanner<Grammar> create(RustSquidConfiguration squidConfig, SquidAstVisitor<Grammar>... visitors) {
        var context = new SquidAstVisitorContextImpl<>(new SourceProject("Rust Project"));
        var parser = RustParser.create(context, squidConfig);
        var builder = AstScanner.<Grammar>builder(context).setBaseParser(parser);

        /* Metrics */
        builder.withMetrics(RustMetric.values());

        /* Files */
        builder.setFilesMetric(RustMetric.FILES);

        /* Comments */
        builder.setCommentAnalyser(
                new CommentAnalyser() {
                    @Override
                    public boolean isBlank(String line) {
                        for (var i = 0; i < line.length(); i++) {
                            if (Character.isLetterOrDigit(line.charAt(i))) {
                                return false;
                            }
                        }
                        return true;
                    }

                    @Override
                    public String getContents(String comment) {
                        var HEADER_LEN = 2;
                        return "/*".equals(comment.substring(0, HEADER_LEN))
                                ? comment.substring(HEADER_LEN, comment.length() - HEADER_LEN)
                                : comment.substring(HEADER_LEN);
                    }
                });


        builder.withSquidAstVisitor(new LinesVisitor<>(RustMetric.LINES));
        builder.withSquidAstVisitor(new RustLinesOfCodeVisitor<>());
        builder.withSquidAstVisitor(CommentsVisitor.<Grammar>builder().withCommentMetric(RustMetric.COMMENT_LINES)
                .withNoSonar(true)
                .build());

        // to emit a 'new file' event to the internals of the plugin
        builder.withSquidAstVisitor(new RustFileVisitor<>());

        /* NCLOC & EXECUTABLE_LINES */
        builder.withSquidAstVisitor(new RustFileLinesVisitor());



        return builder.build();
    }
}
