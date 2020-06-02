package org.elegoff.rust.visitors;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.elegoff.rust.parser.RustParser;
import org.sonar.squidbridge.SquidAstVisitor;

public class RustFileVisitor<GRAMMAR extends Grammar> extends SquidAstVisitor<GRAMMAR> {

    @Override
    public void visitFile(AstNode node) {
        RustParser.finishedParsing(getContext().getFile());
    }

}