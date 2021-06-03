package org.sonar.rust.metrics;

import com.sonar.sslr.api.AstNode;
import org.junit.Test;
import org.sonar.rust.RustFile;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustVisitorContext;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class LinesOfCodeVisitorTest {


    @Test
    public void testVisit(){

        ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(StandardCharsets.UTF_8, RustGrammar.create().build());
        AstNode rootNode = parser.parse("");
        LinesOfCodeVisitor lcv = new LinesOfCodeVisitor(parser);
        RustFile source = new RustFile() {
            @Override
            public String name() {
                return null;
            }

            @Override
            public String content() {
                return "";
            }

            @Override
            public URI uri() {
                return null;
            }
        };
        RustVisitorContext context = new RustVisitorContext(source, rootNode);
       lcv.setContext(context);
        lcv.visitFile(rootNode);
        Set<Integer> lines = lcv.linesOfCode();

        assertThat(lines).hasSize(0);


    }

}
