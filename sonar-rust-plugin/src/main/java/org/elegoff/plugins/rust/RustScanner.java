package org.elegoff.plugins.rust;

import com.sonar.sslr.api.AstNode;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustFile;
import org.sonar.plugins.rust.api.RustVisitorContext;
import org.sonar.plugins.rust.api.tree.FileInput;
import org.sonar.rust.metrics.FileMetrics;
import org.sonar.rust.tree.RustTreeMaker;
import org.sonarsource.analyzer.commons.ProgressReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.sonar.rust.parser.RustParser;

public class RustScanner {
    private static final Logger LOG = Loggers.get(RustScanner.class);
    private static final String FAIL_FAST_PROPERTY_NAME = "sonar.internal.analysis.failFast";
    private final SensorContext context;
    private final RustParser parser;
    private final Map<InputFile, String> packageNames = new HashMap<>();
    private final RustChecks checks;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final NoSonarFilter noSonarFilter;



    public RustScanner(SensorContext context, RustChecks checks, FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, List<InputFile> mainFiles) {
        this.context = context;
        this.checks = checks;
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.noSonarFilter = noSonarFilter;
        this.parser = RustParser.create();

    }

    public void execute(List<InputFile> files, SensorContext context) {
        ProgressReport progressReport = new ProgressReport(this.name() + " progress", TimeUnit.SECONDS.toMillis(10));
        LOG.info("Starting " + this.name());
        List<String> filenames = files.stream().map(InputFile::toString).collect(Collectors.toList());
        progressReport.start(filenames);
        for (InputFile file : files) {
            if (context.isCancelled()) {
                progressReport.cancel();
                return;
            }
            try {
                this.scanFile(file);
            } catch (Exception e) {
                this.processException(e, file);
                if (context.config().getBoolean(FAIL_FAST_PROPERTY_NAME).orElse(false)) {
                    throw new IllegalStateException("Exception when analyzing " + file, e);
                }
            } finally {
                progressReport.nextFile();
            }
        }

        progressReport.stop();
    }

    private String name() {
        return "rules execution";
    }

    public void scanFile(InputFile file) throws IOException{
        RustFile rustFile = new RustFile(file);
        RustVisitorContext visitorContext;
        AstNode astNode = parser.parse(rustFile.contents());
        FileInput parse = new RustTreeMaker().fileInput(astNode);
        visitorContext = new RustVisitorContext(parse, rustFile, context.fileSystem().workDir());
        saveMeasures(rustFile, visitorContext);
    }

    private void saveMeasures(RustFile rustFile, RustVisitorContext visitorContext) {
        FileMetrics fileMetrics = new FileMetrics(visitorContext);
    }

    ;


    /*
    protected void scanFile(InputFile inputFile) {
    ** PythonFile pythonFile = SonarQubePythonFile.create(inputFile);
    ** PythonVisitorContext visitorContext;
    try {
      AstNode astNode = parser.parse(pythonFile.content());
      FileInput parse = new PythonTreeMaker().fileInput(astNode);
      visitorContext = new PythonVisitorContext(parse, pythonFile, getWorkingDirectory(context), packageNames.get(inputFile), projectLevelSymbolTable);
      saveMeasures(inputFile, visitorContext);
    } catch (RecognitionException e) {
      visitorContext = new PythonVisitorContext(pythonFile, e);
      LOG.error("Unable to parse file: " + inputFile.toString());
      LOG.error(e.getMessage());
      context.newAnalysisError()
        .onFile(inputFile)
        .at(inputFile.newPointer(e.getLine(), 0))
        .message(e.getMessage())
        .save();
    }
    List<PythonSubscriptionCheck> checksBasedOnTree = new ArrayList<>();
    for (PythonCheck check : checks.all()) {
      if (check instanceof PythonSubscriptionCheck) {
        checksBasedOnTree.add((PythonSubscriptionCheck) check);
      } else {
        check.scanFile(visitorContext);
      }
    }
    SubscriptionVisitor.analyze(checksBasedOnTree, visitorContext);
    saveIssues(inputFile, visitorContext.getIssues());

    if (visitorContext.rootTree() != null) {
      new SymbolVisitor(context.newSymbolTable().onFile(inputFile)).visitFileInput(visitorContext.rootTree());
      new PythonHighlighter(context, inputFile).scanFile(visitorContext);
    }
  }
     */


    public  void processException(Exception e, InputFile file){
        LOG.warn("Unable to analyze file: " + file.toString(), e);
    };


}
