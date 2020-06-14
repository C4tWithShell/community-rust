package org.sonar.rust.ast;

import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.AnalysisException;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.model.VisitorsBridge;
import org.sonarsource.analyzer.commons.ProgressReport;

import javax.annotation.Nullable;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class RustAstScanner {
    private static final Logger LOG = Loggers.get(RustAstScanner.class);

    private final ActionParser<Tree> parser;
    private final SonarComponents sonarComponents;
    private VisitorsBridge visitor;

    public RustAstScanner(ActionParser<Tree> parser, @Nullable SonarComponents sonarComponents) {
        this.parser = parser;
        this.sonarComponents = sonarComponents;
    }

    public void setVisitorBridge(VisitorsBridge visitorBridge) {
        this.visitor=visitorBridge;
    }

    public void scan(Iterable<InputFile> inputFiles) {
        ProgressReport progressReport = new ProgressReport("Report about progress of Rust AST analyzer", TimeUnit.SECONDS.toMillis(10));
        progressReport.start(Iterables.transform(inputFiles, InputFile::toString));

        boolean successfullyCompleted = false;
        boolean cancelled = false;
        try {
            for (InputFile inputFile : inputFiles) {
                if (analysisCancelled()) {
                    cancelled = true;
                    break;
                }
                simpleScan(inputFile);
                progressReport.nextFile();
            }
            successfullyCompleted = !cancelled;
        } finally {
            if (successfullyCompleted) {
                progressReport.stop();
            } else {
                progressReport.cancel();
            }
            visitor.endOfAnalysis();
        }
    }

    private boolean analysisCancelled() {
        return sonarComponents != null && sonarComponents.analysisCancelled();
    }

    private void simpleScan(InputFile inputFile) {
        visitor.setCurrentFile(inputFile);
        try {
            String fileContent = inputFile.contents();
            Tree ast = parser.parse(fileContent);
            visitor.visitFile(ast);
        } catch (RecognitionException e) {
            checkInterrupted(e);
            LOG.error(String.format("Unable to parse source file : '%s'", inputFile));
            LOG.error(e.getMessage());

            parseErrorWalkAndVisit(e, inputFile);
        } catch (Exception e) {
            checkInterrupted(e);
            throw new AnalysisException(getAnalysisExceptionMessage(inputFile), e);
        } catch (StackOverflowError error) {
            LOG.error(String.format("A stack overflow error occured while analyzing file: '%s'", inputFile), error);
            throw error;
        }
    }

    private static void checkInterrupted(Exception e) {
        Throwable cause = Throwables.getRootCause(e);
        if (cause instanceof InterruptedException || cause instanceof InterruptedIOException) {
            throw new AnalysisException("Analysis cancelled", e);
        }
    }

    private static String getAnalysisExceptionMessage(InputFile file) {
        return String.format("SonarQube is unable to analyze file : '%s'", file);
    }


    private void parseErrorWalkAndVisit(RecognitionException e, InputFile inputFile) {
        try {
            visitor.processRecognitionException(e, inputFile);
        } catch (Exception e2) {
            throw new AnalysisException(getAnalysisExceptionMessage(inputFile), e2);
        }
    }
}
