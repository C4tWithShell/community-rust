package org.elegoff.plugins.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class LineCounter {

    private static final Logger LOG = Loggers.get(LineCounter.class);

    private LineCounter() {
    }

    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile, Metric<T> metric, T value) {
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, RustFile rustFile) {
        InputFile inputFile = rustFile.getInputFile();
        LOG.debug("Count lines in {}", inputFile.uri());
        saveMeasures(
                inputFile,
                new LineCountParser(rustFile.contents()).getLineCountData(),
                fileLinesContextFactory.createFor(inputFile), context);

    }

    private static void saveMeasures(InputFile rustFile, LineCountData data, FileLinesContext fileLinesContext, SensorContext context) {
        for (int line = 1; line <= data.linesNumber(); line++) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, data.linesOfCodeLines().contains(line) ? 1 : 0);
            if (Version.create(7, 3).isGreaterThanOrEqual(context.getSonarQubeVersion())) {
                fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, data.effectiveCommentLines().contains(line) ? 1 : 0);
            }
        }
        fileLinesContext.save();

        saveMeasure(context, rustFile, CoreMetrics.COMMENT_LINES, data.effectiveCommentLines().size());
        saveMeasure(context, rustFile, CoreMetrics.NCLOC, data.linesOfCodeLines().size());
    }








}