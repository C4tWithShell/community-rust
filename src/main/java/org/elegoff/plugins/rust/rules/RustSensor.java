package org.elegoff.plugins.rust.rules;

import org.elegoff.plugins.rust.checks.CheckRepository;
import org.elegoff.plugins.rust.checks.RustCheck;
import org.elegoff.plugins.rust.checks.RustIssue;
import org.elegoff.plugins.rust.checks.RustSourceCode;
import org.elegoff.plugins.rust.highlighting.HighlightingData;
import org.elegoff.plugins.rust.highlighting.RustHighlighting;
import org.elegoff.plugins.rust.languages.RustLanguage;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextPointer;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.elegoff.plugins.rust.linecounter.LineCounter;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
//import org.elegoff.plugins.rust.highlighting.HighlightingData;
//import org.elegoff.plugins.rust.highlighting.RustHighlighting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Main sensor
 */
public class RustSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(RustSensor.class);

    private final Checks<Object> checks;
    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;


    /**
     * Constructor
     *
     * @param fileSystem the file system on which the sensor will find the files to be analyzed
     * @param checkFactory check factory used to get the checks to execute against the files
     * @param fileLinesContextFactory factory used to report measures
     */
    public RustSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.checks = checkFactory.create(CheckRepository.REPOSITORY_KEY).addAnnotatedChecks((Iterable<?>) CheckRepository.getCheckClasses());
        this.fileSystem = fileSystem;

        this.mainFilesPredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.MAIN),
                fileSystem.predicates().hasLanguage(RustLanguage.KEY));

    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY);
        descriptor.name("RUST Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        LOGGER.debug("RUST sensor executed with context: " + context);
        Optional<RuleKey> parsingErrorKey = getParsingErrorRuleKey();

        // Skip analysis if no rules enabled from this plugin
        boolean skipChecks = false;
        if (context.activeRules().findByRepository(CheckRepository.REPOSITORY_KEY).isEmpty()) {
            LOGGER.info("No active rules found for this plugin, skipping.");
            skipChecks = true;
        }

        for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
            LOGGER.debug("Analyzing file: " + inputFile.filename());
            try {
                RustSourceCode sourceCode = new RustSourceCode(inputFile, context.config().getBoolean(RustLanguageSettings.FILTER_UTF8_LB_KEY));
                computeLinesMeasures(context, sourceCode);
                //TODO saveSyntaxHighlighting(context, sourceCode);

                if (!skipChecks) {
                    // First check for syntax errors
                    if (!sourceCode.hasCorrectSyntax()) {
                        LOGGER.debug("File has syntax errors");
                        processAnalysisError(context, sourceCode, inputFile, parsingErrorKey);
                    }
                    runChecks(context, sourceCode);
                }
            } catch (IOException e) {
                LOGGER.warn("Error reading source file " + inputFile.filename(), e);
            }
        }
    }


    /**
     * Calculates and feeds line measures (comments, actual number of code lines)
     *
     *
     * @param context the sensor context
     * @param sourceCode the RUST source code to be analyzed
     */
    private void computeLinesMeasures(SensorContext context, RustSourceCode sourceCode) {
        LineCounter.analyse(context, fileLinesContextFactory, sourceCode);
    }

    /**
     * Returns the {@link RuleKey} of the check that tags syntax errors
     *
     * @return a {@link RuleKey}
     */
    private Optional<RuleKey> getParsingErrorRuleKey() {
        for (Object obj : checks.all()) {
            RustCheck check = (RustCheck) obj;
            if (check.getClass().equals(CheckRepository.getParsingErrorCheckClass())) {
                LOGGER.debug("Parsing error rule key found: " + check.getRuleKey());
                return Optional.of(checks.ruleKey(check));
            }
        }
        LOGGER.debug("No parsing error rule key found");
        return Optional.empty();
    }

    /**
     * Runs all checks (except the syntax check) against the passed RUST source code
     *
     * @param context the sensor context
     * @param sourceCode the source code to be checked
     */
    private void runChecks(SensorContext context, RustSourceCode sourceCode) {
        for (Object check : checks.all()) {
            ((RustCheck) check).setRuleKey(checks.ruleKey(check));
            ((RustCheck) check).setRustSourceCode(sourceCode);
            LOGGER.debug("Checking rule: " + ((RustCheck) check).getRuleKey());
            //TODO ((RustCheck) check).validate();
        }
        saveIssues(context, sourceCode);
    }

    /**
     * Saves the found issues in SonarQube
     *
     * @param context the context
     * @param sourceCode the analyzed RUST source
     */
    private void saveIssues(SensorContext context, RustSourceCode sourceCode) {
        for (RustIssue rustIssue : sourceCode.getRustIssues()) {
            LOGGER.debug("Saving issue: " + rustIssue.getMessage());
            NewIssue newIssue = context.newIssue().forRule(rustIssue.getRuleKey());
            NewIssueLocation location = newIssue.newLocation()
                    .on(sourceCode.getRustFile())
                    .message(rustIssue.getMessage())
                    .at(sourceCode.getRustFile().selectLine(rustIssue.getLine()==0?1:rustIssue.getLine()));
            newIssue.at(location).save();
        }
    }

    /**
     * Saves the syntax highlighting for the analyzed code
     *
     * @param context the sensor context
     * @param sourceCode the RUST source code
     */
    private static void saveSyntaxHighlighting(SensorContext context, RustSourceCode sourceCode) {
        List<HighlightingData> highlightingDataList;
        try {
            highlightingDataList = new RustHighlighting(sourceCode).getHighlightingData();
        } catch (IOException e) {
            throw new IllegalStateException("Could not analyze file " + sourceCode.getRustFile().filename(), e);
        }
        NewHighlighting highlighting = context.newHighlighting().onFile(sourceCode.getRustFile());

        for (HighlightingData highlightingData : highlightingDataList) {
            highlightingData.highlight(highlighting);
        }
        highlighting.save();
    }

    /**
     * Reports the passed issue as a syntax/parse error (aka {@link org.sonar.api.batch.sensor.error.AnalysisError} in
     * the SonarQube terminology)
     *
     * @param context the sensor context
     * @param sourceCode the analyzed RUST source
     * @param inputFile the file that contains the error
     * @param parsingErrorKey the {@link RuleKey} of the check that corresponds to a syntax error. If present, an issue
     *                        is reported as well as the analysis error.
     */
    private static void processAnalysisError(SensorContext context, RustSourceCode sourceCode, InputFile inputFile, Optional<RuleKey> parsingErrorKey) {
        final RustIssue error = sourceCode.getSyntaxError();

        LOGGER.warn("Syntax error in file: {}", inputFile.filename());
        LOGGER.warn("Cause: {} at line {}, column {}", error.getMessage(), error.getLine(), error.getColumn());

        LOGGER.debug("Creating analysis error");
        context.newAnalysisError()
                .onFile(inputFile)
                .message(sourceCode.getSyntaxError().getMessage())
                .at(new TextPointer() {
                    @Override
                    public int line() {
                        return error.getLine();
                    }

                    @Override
                    public int lineOffset() {
                        return error.getColumn();
                    }

                    @Override
                    public int compareTo(TextPointer textPointer) {
                        return textPointer.line() - line();
                    }
                })
                .save();

        if (parsingErrorKey.isPresent()) {
            LOGGER.debug("parsingErrorKey present, creating issue");
            // the ParsingErrorCheck rule is activated: we create a beautiful issue
            NewIssue newIssue = context.newIssue().forRule(parsingErrorKey.get());
            NewIssueLocation location = newIssue.newLocation()
                    .message("Parse error: " + error.getMessage())
                    .on(inputFile)
                    .at(sourceCode.getRustFile().selectLine(error.getLine()));
            newIssue.at(location).save();
        }
    }
}