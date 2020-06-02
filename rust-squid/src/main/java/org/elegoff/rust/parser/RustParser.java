package org.elegoff.rust.parser;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.impl.Parser;
import org.elegoff.rust.RustSquidConfiguration;
import org.elegoff.rust.lexer.RustLexer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.squidbridge.SquidAstVisitorContext;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceProject;

import java.io.File;
import java.lang.ref.WeakReference;

public class RustParser {



    private RustParser() {
    }

    public static void finishedParsing(File path) {

    }

    public static Parser<Grammar> create() {
        return create(new SquidAstVisitorContextImpl<>(new SourceProject("")),
                new RustSquidConfiguration());
    }

    public static Parser<Grammar> create(SquidAstVisitorContext<Grammar> context) {
        return create(context, new RustSquidConfiguration());
    }

    public static Parser<Grammar> create(SquidAstVisitorContext<Grammar> context, RustSquidConfiguration squidConfig) {

        return Parser.builder(RustGrammarImpl.create(squidConfig))
                .withLexer(RustLexer.create(squidConfig))
                .build();
    }

}
