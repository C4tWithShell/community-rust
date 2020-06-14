package org.sonar.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.ast.visitors.LinesOfCodeVisitor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Measurer extends SubscriptionVisitor{

    private final SensorContext sensorContext;
    private final NoSonarFilter noSonarFilter;
    private InputFile sonarFile;

    public Measurer(SensorContext context, NoSonarFilter noSonarFilter) {
        this.sensorContext = context;
        this.noSonarFilter = noSonarFilter;
    }

    @Override
    public List<Tree.Kind> nodesToVisit() {
        return Arrays.asList(Tree.Kind.EXPRESSION);
    }

    @Override
    public void scanFile(RustFileScannerContext context) {
        sonarFile = context.getInputFile();
        //CommentLinesVisitor commentLinesVisitor = createCommentLineVisitorAndFindNoSonar(context);
        super.setContext(context);
        scanTree(context.getTree());

        //int fileComplexity = context.getComplexityNodes(context.getTree()).size();
        saveMetricOnFile(CoreMetrics.NCLOC, new LinesOfCodeVisitor().linesOfCode(context.getTree()));
    }

    private <T extends Serializable> void saveMetricOnFile(Metric<T> metric, T value) {
        sensorContext.<T>newMeasure().forMetric(metric).on(sonarFile).withValue(value).save();
    }
}
