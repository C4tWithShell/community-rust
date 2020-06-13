package org.sonar.rust.model;

import com.sonar.sslr.api.RecognitionException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustCheck;
import org.sonar.plugins.rust.api.RustFileScanner;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.EndOfAnalysisCheck;
import org.sonar.rust.SonarComponents;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class VisitorsBridge {

    private static final Logger LOG = Loggers.get(VisitorsBridge.class);

    private final SonarComponents sonarComponents;
    private Set<String> classesNotFound = new TreeSet<>();
    private final List<RustFileScanner> allScanners;

    public VisitorsBridge( Iterable visitors, SonarComponents sonarComponents) {
        this.sonarComponents = sonarComponents;
        this.allScanners = new ArrayList<>();
        for (Object visitor : visitors) {
            if (visitor instanceof RustFileScanner) {
                allScanners.add((RustFileScanner) visitor);
            }
        }
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

    public void visitFile(Tree ast) {
        //TODO
    }

    public void processRecognitionException(RecognitionException e, InputFile inputFile) {
        //TODO
    }
}
