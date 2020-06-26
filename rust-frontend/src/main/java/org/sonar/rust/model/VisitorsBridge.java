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
package org.sonar.rust.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Throwables;
import com.sonar.sslr.api.RecognitionException;
import org.sonar.api.batch.fs.InputFile;

import org.sonar.api.utils.AnnotationUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;
import org.sonar.plugins.rust.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.rust.api.RustFileScanner;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.*;
import org.sonar.rust.resolve.SemanticModel;
import org.sonar.rust.tree.SyntaxToken;

import javax.annotation.Nullable;
import java.io.File;
import java.io.InterruptedIOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VisitorsBridge {

    private static final Logger LOG = Loggers.get(VisitorsBridge.class);

    private final SonarComponents sonarComponents;
    private Set<String> classesNotFound = new TreeSet<>();
    private final List<RustFileScanner> allScanners;
    private SemanticModel semanticModel;
    protected InputFile currentFile;
    private List<RustFileScanner> executableScanners;
    private ScannerRunner scannerRunner;
    private static Predicate<RustFileScanner> isIssuableSubscriptionVisitor = s -> s instanceof IssuableSubscriptionVisitor;

    public VisitorsBridge( Iterable visitors, SonarComponents sonarComponents) {
        this.sonarComponents = sonarComponents;
        this.allScanners = new ArrayList<>();
        for (Object visitor : visitors) {
            if (visitor instanceof RustFileScanner) {
                allScanners.add((RustFileScanner) visitor);
            }
        }
    }

    @VisibleForTesting
    public VisitorsBridge(RustFileScanner visitor) {
        this(Collections.singletonList(visitor),  null);
    }




    public void endOfAnalysis() {
        if(!classesNotFound.isEmpty()) {
            String message = "";
            if(classesNotFound.size() > 50) {
                message = ", ...";
            }
            LOG.warn("Not found during the analysis : [{}{}]", classesNotFound.stream().limit(50).collect(Collectors.joining(", ")), message);
        }
        allScanners.stream()
                .filter(s -> s instanceof EndOfAnalysisCheck)
                .map(EndOfAnalysisCheck.class::cast)
                .forEach(EndOfAnalysisCheck::endOfAnalysis);

    }

    public void setCurrentFile(InputFile inputFile) {
        //TODO
    }

    public void visitFile(@Nullable Tree parsedTree) {
        semanticModel = null;
        CompilationUnitTree tree = new RustTree.CompilationUnitTreeImpl(null, null);
        boolean fileParsed = parsedTree != null;
        if (fileParsed && parsedTree.is(Tree.Kind.COMPILATION_UNIT)) {
            tree = (CompilationUnitTree) parsedTree;

                SemanticModel.handleMissingTypes(tree);

        }
        RustFileScannerContext rustFileScannerContext = createScannerContext(tree, semanticModel, sonarComponents, fileParsed);

        executableScanners.forEach(scanner -> runScanner(rustFileScannerContext, scanner, AnalysisError.Kind.CHECK_ERROR));
        scannerRunner.run(rustFileScannerContext);

    }

    public void processRecognitionException(RecognitionException e, InputFile inputFile) {
        //TODO
    }

    protected RustFileScannerContext createScannerContext(
            CompilationUnitTree tree, SemanticModel semanticModel, SonarComponents sonarComponents, boolean fileParsed) {
        return new DefaultRustFileScannerContext(
                tree,
                currentFile,
                semanticModel,
                sonarComponents,
                fileParsed);
    }

    private void runScanner(RustFileScannerContext javaFileScannerContext, RustFileScanner scanner, AnalysisError.Kind kind) {
        try {
            scanner.scanFile(javaFileScannerContext);
        } catch (IllegalRuleParameterException e) {
            // bad configuration of a rule parameter, we want to fail analysis fast.
            throw e;
        } catch (Exception e) {
            if (sonarComponents != null && sonarComponents.shouldFailAnalysisOnException()) {
                throw e;
            }
            Throwable rootCause = Throwables.getRootCause(e);
            if (rootCause instanceof InterruptedIOException || rootCause instanceof InterruptedException) {
                throw e;
            }
            Rule annotation = AnnotationUtils.getAnnotation(scanner.getClass(), Rule.class);
            String key = "";
            if (annotation != null) {
                key = annotation.key();
            }
            LOG.error(
                    String.format("Unable to run check %s - %s on file '%s', To help improve SonarJava, please report this problem to SonarSource : see https://www.sonarqube.org/community/",
                            scanner.getClass(), key, currentFile),
                    e);
            addAnalysisError(e, currentFile, kind);
        }
    }

    private void addAnalysisError(Exception e, InputFile inputFile, AnalysisError.Kind checkError) {
        if (sonarComponents != null) {
            sonarComponents.addAnalysisError(new AnalysisError(e, inputFile.toString(), checkError));
        }
    }
    private static class ScannerRunner {
        private EnumMap<Tree.Kind, List<SubscriptionVisitor>> checks;
        private List<SubscriptionVisitor> subscriptionVisitors;

        ScannerRunner(List<RustFileScanner> executableScanners) {
            checks = new EnumMap<>(Tree.Kind.class);
            subscriptionVisitors = executableScanners.stream()
                    .filter(isIssuableSubscriptionVisitor)
                    .map(s -> (SubscriptionVisitor) s)
                    .collect(Collectors.toList());
            subscriptionVisitors.forEach(s -> s.nodesToVisit().forEach(k -> checks.computeIfAbsent(k, key -> new ArrayList<>()).add(s))
            );
        }

        public void run(RustFileScannerContext rustFileScannerContext) {
            subscriptionVisitors.forEach(s -> s.setContext(rustFileScannerContext));
            visit(rustFileScannerContext.getTree());
            subscriptionVisitors.forEach(s -> s.leaveFile(rustFileScannerContext));
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

        private void visit(Tree tree) {
            Consumer<SubscriptionVisitor> callback;
            boolean isToken = tree.kind() == Tree.Kind.TOKEN;
            if (isToken) {
                callback = s -> {
                    SyntaxToken syntaxToken = (SyntaxToken) tree;
                    s.visitToken(syntaxToken);
                };
            } else {
                callback = s -> s.visitNode(tree);
            }
            List<SubscriptionVisitor> subscribed = checks.getOrDefault(tree.kind(), Collections.emptyList());
            subscribed.forEach(callback);
            if (isToken) {
                checks.getOrDefault(Tree.Kind.TRIVIA, Collections.emptyList()).forEach(s -> ((SyntaxToken) tree).trivias().forEach(s::visitTrivia));
            } else {
                visitChildren(tree);
            }
            if(!isToken) {
                subscribed.forEach(s -> s.leaveNode(tree));
            }
        }
    }
}
