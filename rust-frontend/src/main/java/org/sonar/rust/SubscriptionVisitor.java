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
package org.sonar.rust;

import org.sonar.plugins.rust.api.RustFileScanner;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.RustSubscriptionCheck;
import org.sonar.plugins.rust.api.RustVisitorContext;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.model.RustTree;
import org.sonar.rust.resolve.SemanticModel;
import org.sonar.rust.tree.SyntaxToken;
import org.sonar.rust.tree.SyntaxTrivia;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public abstract class SubscriptionVisitor implements RustFileScanner {
    protected RustFileScannerContext context;
    private EnumSet<Tree.Kind> nodesToVisit;
    private boolean visitToken;
    private boolean visitTrivia;
    private SemanticModel semanticModel;

    public abstract List<Tree.Kind> nodesToVisit();

    public void setContext(RustFileScannerContext context) {
        this.context = context;
        semanticModel = (SemanticModel) context.getSemanticModel();
    }


    @Override
    public void scanFile(RustFileScannerContext context) {
        setContext(context);
        scanTree(context.getTree());
    }

    protected void scanTree(Tree tree) {
        if(nodesToVisit == null) {
            List<Tree.Kind> kinds = nodesToVisit();
            if(kinds.isEmpty()) {
                nodesToVisit = EnumSet.noneOf(Tree.Kind.class);
            } else {
                nodesToVisit = EnumSet.copyOf(kinds);
            }
        }
        visitToken = isVisitingTokens();
        visitTrivia = isVisitingTrivia();
        visit(tree);
    }

    private boolean isVisitingTrivia() {
        return nodesToVisit.contains(Tree.Kind.TRIVIA);
    }

    private boolean isVisitingTokens() {
        return nodesToVisit.contains(Tree.Kind.TOKEN);
    }

    private void visit(Tree tree) {
        boolean isSubscribed = isSubscribed(tree);
        boolean shouldVisitSyntaxToken = (visitToken || visitTrivia) && tree.is(Tree.Kind.TOKEN);
        if (shouldVisitSyntaxToken) {
            SyntaxToken syntaxToken = (SyntaxToken) tree;
            if (visitToken) {
                visitToken(syntaxToken);
            }
            if (visitTrivia) {
                for (SyntaxTrivia syntaxTrivia : syntaxToken.trivias()) {
                    visitTrivia(syntaxTrivia);
                }
            }
        } else if (isSubscribed) {
            visitNode(tree);
        }
        visitChildren(tree);
        if (!shouldVisitSyntaxToken && isSubscribed) {
            leaveNode(tree);
        }
    }
    private void visitChildren(Tree tree) {
        RustTree rustTree = (RustTree) tree;
        if (!rustTree.isLeaf()) {
            for (Tree next : rustTree.getChildren()) {
                if (next != null) {
                    visit(next);
                }
            }
        }
    }
    public void visitNode(Tree tree) {
        //Default behavior : do nothing.
    }

    public void leaveNode(Tree tree) {
        //Default behavior : do nothing.
    }

    public void visitToken(SyntaxToken syntaxToken) {
        //default behaviour is to do nothing
    }

    public void visitTrivia(SyntaxTrivia syntaxTrivia) {
        //default behaviour is to do nothing
    }
    private boolean isSubscribed(Tree tree) {
        return nodesToVisit.contains(tree.kind());
    }


    public void leaveFile(RustFileScannerContext context) {
        //default behaviour is to do nothing
    }
}
