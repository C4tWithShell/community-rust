/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
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
