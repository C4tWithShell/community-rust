package org.elegoff.plugins.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        LOG.debug("Count lines in {}", rustFile.getInputFile().uri());

        Set<Integer> linesOfCode = new HashSet<>();
        Set<Integer> commentLines = new HashSet<>();



        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(rustFile.getInputFile());
        linesOfCode.forEach(lineOfCode -> fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, lineOfCode, 1));
        fileLinesContext.save();

        saveMeasure(context, rustFile.getInputFile(), CoreMetrics.COMMENT_LINES, commentLines.size());
        saveMeasure(context, rustFile.getInputFile(), CoreMetrics.NCLOC, linesOfCode.size());

    }





    private static void addLinesRange(Set<Integer> set, int start, int end) {
        for (int line = start; line <= end; line++) {
            set.add(line);
        }
    }



}