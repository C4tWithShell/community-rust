package org.elegoff.plugins.rust;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.RecognitionException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustFile;
import org.sonar.plugins.rust.api.RustVisitorContext;
import org.sonar.plugins.rust.api.tree.FileInput;
import org.sonar.rust.metrics.FileLinesVisitor;
import org.sonar.rust.metrics.FileMetrics;
import org.sonar.rust.tree.RustTreeMaker;
import org.sonarsource.analyzer.commons.ProgressReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        try {
            AstNode astNode = parser.parse(rustFile.contents());
            FileInput parse = new RustTreeMaker().fileInput(astNode);
            visitorContext = new RustVisitorContext(parse, rustFile, context.fileSystem().workDir());
            saveMeasures(file, visitorContext);
        }catch (RecognitionException e) {
            visitorContext = new RustVisitorContext(rustFile, e);
            LOG.error("Unable to parse file: " + file.toString());
            LOG.error(e.getMessage());
            context.newAnalysisError()
                    .onFile(file)
                    .at(file.newPointer(e.getLine(), 0))
                    .message(e.getMessage())
                    .save();
        }
    }

    private void saveMeasures(InputFile inputFile, RustVisitorContext visitorContext) {
        FileMetrics fileMetrics = new FileMetrics(visitorContext);
        FileLinesVisitor fileLinesVisitor = fileMetrics.fileLinesVisitor();
        Set<Integer> linesOfCode = fileLinesVisitor.getLinesOfCode();
        saveMetricOnFile(inputFile, CoreMetrics.NCLOC, linesOfCode.size());
    }

    public  void processException(Exception e, InputFile file){
        LOG.warn("Unable to analyze file: " + file.toString(), e);
    };

    private void saveMetricOnFile(InputFile inputFile, Metric<Integer> metric, Integer value) {
        context.<Integer>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }


}
