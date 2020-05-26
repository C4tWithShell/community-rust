package org.elegoff.plugins.rust.clippy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


import org.elegoff.plugins.rust.languages.RustLanguage;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewExternalIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.analyzer.commons.ExternalReportProvider;
import org.sonarsource.analyzer.commons.internal.json.simple.parser.ParseException;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class ClippySensor implements Sensor {

    private static final Logger LOG = Loggers.get(ClippySensor.class);
    static final String LINTER_KEY = "clippy";
    static final String LINTER_NAME = "Clippy";
    public static final String REPORT_PROPERTY_KEY = "sonar.rust.clippy.reportPaths";
    private static final Long DEFAULT_CONSTANT_DEBT_MINUTES = 5L;

    @Override
    public void execute(SensorContext context) {
        Set<String> unresolvedInputFiles = new HashSet<>();
        List<File> reportFiles = ExternalReportProvider.getReportFiles(context, reportPathKey());
        reportFiles.forEach(report -> importReport(report, context, unresolvedInputFiles));
        logUnresolvedInputFiles(unresolvedInputFiles);
    }

    private void importReport(File rawReport, SensorContext context, Set<String> unresolvedInputFiles) {
        LOG.info("Importing {}", rawReport);

        try {
            InputStream in = ClippyJsonReportReader.toJSON(rawReport);
            ClippyJsonReportReader.read(in, clippyIssue -> saveIssue(context, clippyIssue, unresolvedInputFiles));
        } catch (IOException | ParseException e) {
            LOG.error("No issues information will be saved as the report file '{}' can't be read. " +
                    e.getClass().getSimpleName() + ": " + e.getMessage(), rawReport, e);
        }
    }

    private void importReportObsolete(File reportPath, SensorContext context, Set<String> unresolvedInputFiles) {
        try (InputStream in = new FileInputStream(reportPath)) {
            LOG.info("Importing {}", reportPath);

            ClippyJsonReportReader.read(in, clippyIssue -> saveIssue(context, clippyIssue, unresolvedInputFiles));
        } catch (IOException | ParseException | RuntimeException e) {
            LOG.error("No issues information will be saved as the report file '{}' can't be read. " +
                    e.getClass().getSimpleName() + ": " + e.getMessage(), reportPath, e);
        }
    }

    private static void saveIssue(SensorContext context, ClippyJsonReportReader.ClippyIssue clippyIssue, Set<String> unresolvedInputFiles) {
        if (isEmpty(clippyIssue.ruleKey) || isEmpty(clippyIssue.filePath) || isEmpty(clippyIssue.message)) {
            LOG.debug("Missing information for ruleKey:'{}', filePath:'{}', message:'{}'", clippyIssue.ruleKey, clippyIssue.filePath, clippyIssue.message);
            return;
        }

        InputFile inputFile = context.fileSystem().inputFile(context.fileSystem().predicates().hasPath(clippyIssue.filePath));
        if (inputFile == null) {
            unresolvedInputFiles.add(clippyIssue.filePath);
            return;
        }

        NewExternalIssue newExternalIssue = context.newExternalIssue();
        newExternalIssue
                .type(RuleType.CODE_SMELL)
                .severity(toSonarQubeSeverity(clippyIssue.severity))
                .remediationEffortMinutes(DEFAULT_CONSTANT_DEBT_MINUTES);

        NewIssueLocation primaryLocation = newExternalIssue.newLocation()
                .message(clippyIssue.message)
                .on(inputFile);

        if (clippyIssue.lineNumberStart != null) {
            primaryLocation.at(inputFile.newRange(clippyIssue.lineNumberStart, clippyIssue.colNumberStart - 1, clippyIssue.lineNumberEnd, clippyIssue.colNumberEnd - 1));
        }

        newExternalIssue.at(primaryLocation);
        newExternalIssue.engineId(LINTER_KEY).ruleId(clippyIssue.ruleKey);
        newExternalIssue.save();
    }

    private static Severity toSonarQubeSeverity(String severity) {
        if ("error".equalsIgnoreCase(severity)) {
            return Severity.MAJOR;
        } else
            return Severity.MINOR;
    }


    private static RuleType toSonarQubeType(String severity) {
        if ("error".equalsIgnoreCase(severity)) {
            return RuleType.BUG;
        } else if ("help".equalsIgnoreCase(severity)) {
            return RuleType.CODE_SMELL;
        } else {
            return RuleType.CODE_SMELL;
        }
    }

    private static final int MAX_LOGGED_FILE_NAMES = 20;

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyWhenConfiguration(conf -> conf.hasKey(reportPathKey()))
                .onlyOnLanguage(RustLanguage.KEY)
                .name("Import of " + linterName() + " issues");
    }


    private void logUnresolvedInputFiles(Set<String> unresolvedInputFiles) {
        if (unresolvedInputFiles.isEmpty()) {
            return;
        }
        String fileList = unresolvedInputFiles.stream().sorted().limit(MAX_LOGGED_FILE_NAMES).collect(Collectors.joining(";"));
        if (unresolvedInputFiles.size() > MAX_LOGGED_FILE_NAMES) {
            fileList += ";...";
        }
        logger().warn("Failed to resolve {} file path(s) in " + linterName() + " report. No issues imported related to file(s): {}", unresolvedInputFiles.size(), fileList);
    }

    protected String linterName() {
        return LINTER_NAME;
    }

    protected String reportPathKey() {
        return REPORT_PROPERTY_KEY;
    }

    protected Logger logger() {
        return LOG;
    }
}