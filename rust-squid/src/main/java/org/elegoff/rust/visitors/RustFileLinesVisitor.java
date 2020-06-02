/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2020 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-Rust
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
package org.elegoff.rust.visitors;

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import java.util.ArrayList;
import java.util.List;

import org.elegoff.rust.parser.RustGrammarImpl;
import org.sonar.api.measures.CoreMetrics;
import org.elegoff.rust.api.RustKeyword;
import org.elegoff.rust.api.RustMetric;
import org.elegoff.rust.api.RustPunctuator;

import org.sonar.squidbridge.SquidAstVisitor;

/**
 * Visitor that computes {@link CoreMetrics#NCLOC_DATA_KEY} and {@link CoreMetrics#EXECUTABLE_LINES_DATA} metrics used
 * by the DevCockpit.
 */
public class RustFileLinesVisitor extends SquidAstVisitor<Grammar> implements AstAndTokenVisitor {

        private List<Integer> linesOfCode;
        private List<Integer> executableLines;
        private int isWithinFunctionDefinition;

        private static boolean isDefaultOrDeleteFunctionBody(AstNode astNode) {
                AstNode node = astNode.getFirstChild(RustGrammarImpl.functionBody);
                if ((node != null)) {
                        List<AstNode> functionBody = node.getChildren();

                        // look for exact sub AST
                        if ((functionBody.size() == 3) && functionBody.get(0).is(RustPunctuator.ASSIGN)
                                && functionBody.get(2).is(RustPunctuator.SEMICOLON)) {
                                AstNode bodyType = functionBody.get(1);
                                if (bodyType.is(RustKeyword.DELETE)
                                        || bodyType.is(RustKeyword.DEFAULT)) {
                                        return true;
                                }
                        }
                }
                return false;
        }

        static boolean isCodeToken(Token token) {
                final TokenType type = token.getType();
                if (!(type instanceof RustPunctuator)) {
                        return true;
                }

                switch ((RustPunctuator) type) {
                        case SEMICOLON:
                        case BR_LEFT:
                        case BR_RIGHT:
                        case CURLBR_LEFT:
                        case CURLBR_RIGHT:
                        case SQBR_LEFT:
                        case SQBR_RIGHT:
                                return false;

                        default:
                                return true;
                }
        }

        static boolean isExecutableToken(Token token) {
                final TokenType type = token.getType();
                return !RustPunctuator.CURLBR_LEFT.equals(type) && !RustKeyword.DEFAULT.equals(type) && !RustKeyword.CASE.equals(type);
        }

        static void addLineNumber(List<Integer> collection, int lineNr) {
                // use the fact, that we iterate over tokens from top to bottom.
                // if the line was already visited its index was put at the end of
                // collection.
                //
                // don't use Set, because Set would sort elements on each insert
                // since we potentially insert line number for each token it would create
                // large run-time overhead
                //
                // we sort/filter duplicates only once - on leaveFile(AstNode)
                //
                if (collection.isEmpty() || collection.get(collection.size() - 1) != lineNr) {
                        collection.add(lineNr);
                }
        }

        @Override
        public void init() {
                subscribeTo(RustGrammarImpl.functionDefinition,
                        RustGrammarImpl.labeledStatement,
                        RustGrammarImpl.expressionStatement,
                        RustGrammarImpl.iterationStatement,
                        RustGrammarImpl.jumpStatement,
                        RustGrammarImpl.assignmentExpression,
                        RustGrammarImpl.lambdaExpression);
        }

        @Override
        public void visitToken(Token token) {
                if (token.getType().equals(GenericTokenType.EOF)) {
                        return;
                }

                if ((isWithinFunctionDefinition != 0) && isCodeToken(token)) {
                        addLineNumber(linesOfCode, token.getLine());
                }
        }

        @Override
        public void visitNode(AstNode astNode) {
                switch ((RustGrammarImpl) astNode.getType()) {
                        case functionDefinition:
                                if (!isDefaultOrDeleteFunctionBody(astNode)) {
                                        increaseFunctionDefinition();
                                }
                                break;
                        case labeledStatement:
                        case expressionStatement:
                        case iterationStatement:
                        case jumpStatement:
                        case assignmentExpression:
                        case lambdaExpression:
                                visitStatement(astNode);
                                break;
                        default:
                                // Do nothing particular
                }
        }

        @Override
        public void leaveNode(AstNode astNode) {
                if (!isDefaultOrDeleteFunctionBody(astNode)) {
                        decreaseFunctionDefinitions();
                }
        }

        @Override
        public void visitFile(AstNode astNode) {
                linesOfCode = new ArrayList<>();
                executableLines = new ArrayList<>();
        }

        @Override
        public void leaveFile(AstNode astNode) {
                getContext().peekSourceCode().addData(RustMetric.NCLOC_DATA, linesOfCode);
                linesOfCode = null;
                getContext().peekSourceCode().addData(RustMetric.EXECUTABLE_LINES_DATA, executableLines);
                executableLines = null;
        }

        /**
         * @param astNode
         */
        private void visitStatement(AstNode astNode) {
                if (astNode.hasDirectChildren(RustGrammarImpl.declarationStatement)
                        && !astNode.hasDescendant(RustGrammarImpl.initializer)) {
                        return;
                }
                if (isExecutableToken(astNode.getToken())) {
                        addLineNumber(executableLines, astNode.getTokenLine());
                }
        }

        private void increaseFunctionDefinition() {
                isWithinFunctionDefinition++;
        }

        private void decreaseFunctionDefinitions() {
                isWithinFunctionDefinition--;
        }

}
