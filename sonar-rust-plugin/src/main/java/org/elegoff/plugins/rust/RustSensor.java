/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
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
package org.elegoff.plugins.rust;


import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.elegoff.rust.checks.Issue;
import org.elegoff.rust.checks.RustCheck;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.rust.RustLexer;
import org.sonar.rust.RustParser;
import org.sonar.rust.RustParserConfiguration;
import org.sonar.rust.RustVisitorContext;
import org.sonar.rust.metrics.MetricsVisitor;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class RustSensor implements Sensor {

    private static final Logger LOG = Loggers.get(RustSensor.class);

    private final FileLinesContextFactory fileLinesContextFactory;
    private final Checks<RustCheck> checks;

    public RustSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.checks = checkFactory
                .<RustCheck>create(CheckList.REPOSITORY_KEY)
                .addAnnotatedChecks((Iterable) CheckList.getRustChecks());
        this.fileLinesContextFactory = fileLinesContextFactory;
    }

    private static void logParseError(SensorContext sensorContext, InputFile inputFile, RecognitionException e) {
        LOG.error("Unable to parse file: " + inputFile);
        LOG.error(e.getMessage());

        sensorContext.newAnalysisError()
                .onFile(inputFile)
                .message(e.getMessage())
                // we can't easily report the precise line when %INCLUDE is used
                // and reporting an incorrect line can cause a failure (SONARPLI-198)
                .save();
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .name("RustSensor")
                .onlyOnLanguage(RustLanguage.KEY)
                .onlyOnFileType(InputFile.Type.MAIN);
    }

    @Override
    public void execute(SensorContext context) {
        final FileSystem fileSystem = context.fileSystem();
        final FilePredicate mainFilePredicates = fileSystem.predicates().and(
                fileSystem.predicates().hasLanguage(RustLanguage.KEY),
                fileSystem.predicates().hasType(InputFile.Type.MAIN));

        RustParserConfiguration parserConfiguration = new RustPluginConfiguration(context.config()).getParserConfiguration(fileSystem.encoding());
        Parser<Grammar> parser = RustParser.create(parserConfiguration);
        MetricsVisitor metricsVisitor = new MetricsVisitor(parserConfiguration);
        RustTokensVisitor tokensVisitor = new RustTokensVisitor(context, RustLexer.create(parserConfiguration));

        Collection<RustCheck> rustChecks = checks.all();

        for (InputFile file : fileSystem.inputFiles(mainFilePredicates)) {
            scanFile(context, file, metricsVisitor, tokensVisitor, parser,rustChecks );
            if (context.isCancelled()) {
                return;
            }
        }
    }

    private void scanFile(
            SensorContext sensorContext,
            InputFile inputFile,
            MetricsVisitor metricsVisitor,
            RustTokensVisitor tokensVisitor,
            Parser<Grammar> parser,
            Collection<RustCheck> checks) {

        File file = inputFile.file();
        SonarQubeRustFile rustFile = SonarQubeRustFile.create(inputFile);
        RustVisitorContext visitorContext;
        LOG.debug("Rust parsing " +  file.getPath());
        try {
            AstNode tree = parser.parse(file);
            visitorContext = new RustVisitorContext(rustFile, tree);

            saveMetrics(sensorContext, inputFile, visitorContext, metricsVisitor);
            tokensVisitor.scanFile(inputFile, visitorContext);

        } catch (RecognitionException e) {
            visitorContext = new RustVisitorContext(rustFile, e);
            logParseError(sensorContext, inputFile, e);
        }

        for (RustCheck check : checks) {
            saveIssues(sensorContext, inputFile, check, check.scanFileForIssues(visitorContext));
        }
    }

    private void saveMetrics(SensorContext context, InputFile inputFile, RustVisitorContext visitorContext, MetricsVisitor metricsVisitor) {
        metricsVisitor.scanFile(visitorContext);
        context.<Integer>newMeasure()
                .on(inputFile)
                .forMetric(CoreMetrics.NCLOC)
                .withValue(metricsVisitor.linesOfCode().size())
                .save();
        context.<Integer>newMeasure()
                .on(inputFile)
                .forMetric(CoreMetrics.STATEMENTS)
                .withValue(metricsVisitor.numberOfStatements())
                .save();
        context.<Integer>newMeasure()
                .on(inputFile)
                .forMetric(CoreMetrics.COMPLEXITY)
                .withValue(metricsVisitor.complexity())
                .save();
        context.<Integer>newMeasure()
                .on(inputFile)
                .forMetric(CoreMetrics.COMMENT_LINES)
                .withValue(metricsVisitor.commentLines().size())
                .save();
        context.<Integer>newMeasure()
                .on(inputFile)
                .forMetric(CoreMetrics.FUNCTIONS)
                .withValue(metricsVisitor.numberOfFunctions())
                .save();

        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);
        for (Integer line : metricsVisitor.linesOfCode()) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
        }
        fileLinesContext.save();
    }

    private void saveIssues(SensorContext context, InputFile inputFile, RustCheck check, List<Issue> issues) {
        RuleKey ruleKey = checks.ruleKey(check);
        for (Issue rustIssue : issues) {
            NewIssue issue = context.newIssue();
            NewIssueLocation location = issue.newLocation()
                    .on(inputFile)
                    .message(rustIssue.message());
            if (rustIssue.line() != null) {
                location.at(inputFile.selectLine(rustIssue.line()));
            }
            issue.at(location)
                    .forRule(ruleKey)
                    .save();
        }
    }

}