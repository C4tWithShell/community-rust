package org.elegoff.rust.visitors;

import com.sonar.sslr.api.Grammar;
import org.elegoff.rust.utils.RustReportIssue;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.squidbridge.SquidAstVisitorContext;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.squidbridge.measures.CalculatedMetricFormula;
import org.sonar.squidbridge.measures.MetricDef;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class MultiLocatitionSquidCheck<G extends Grammar> extends SquidCheck<G> {


    @SuppressWarnings("unchecked")
    public static Set<RustReportIssue> getMultiLocationCheckMessages(SourceFile sourceFile) {
        return (Set<RustReportIssue>) sourceFile.getData(DataKey.FILE_VIOLATIONS_WITH_MULTIPLE_LOCATIONS);
    }


    public static boolean hasMultiLocationCheckMessages(SourceFile sourceFile) {
        Set<RustReportIssue> issues = getMultiLocationCheckMessages(sourceFile);
        return issues != null && !issues.isEmpty();
    }

    public static void eraseMultilineCheckMessages(SourceFile sourceFile) {
        setMultiLocationViolation(sourceFile, null);
    }

    private static void setMultiLocationViolation(SourceFile sourceFile, @Nullable Set<RustReportIssue> messages) {
        sourceFile.addData(DataKey.FILE_VIOLATIONS_WITH_MULTIPLE_LOCATIONS, messages);
    }

    private SourceFile getSourceFile() {
        final SquidAstVisitorContext<G> c = getContext();
        if (c.peekSourceCode() instanceof SourceFile) {
            return (SourceFile) c.peekSourceCode();
        } else if (c.peekSourceCode().getParent(SourceFile.class) != null) {
            return c.peekSourceCode().getParent(SourceFile.class);
        } else {
            throw new IllegalStateException("Unable to get SourceFile on source code '"
                    + (c.peekSourceCode() == null ? "[NULL]" : c.peekSourceCode().getKey()) + "'");
        }
    }


    protected String getRuleKey() {
        org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation(this, org.sonar.check.Rule.class);
        if (ruleAnnotation != null && ruleAnnotation.key() != null) {
            return ruleAnnotation.key();
        }
        throw new IllegalStateException("Check must be annotated with @Rule( key = <key> )");
    }


    protected void createMultiLocationViolation(RustReportIssue message) {
        SourceFile sourceFile = getSourceFile();
        Set<RustReportIssue> messages = getMultiLocationCheckMessages(sourceFile);
        if (messages == null) {
            messages = new HashSet<>();
        }
        messages.add(message);
        setMultiLocationViolation(sourceFile, messages);
    }

    private static enum DataKey implements MetricDef {
        FILE_VIOLATIONS_WITH_MULTIPLE_LOCATIONS;

        @Override
        public String getName() {
            return FILE_VIOLATIONS_WITH_MULTIPLE_LOCATIONS.getName();
        }

        @Override
        public boolean isCalculatedMetric() {
            return false;
        }

        @Override
        public boolean aggregateIfThereIsAlreadyAValue() {
            return false;
        }

        @Override
        public boolean isThereAggregationFormula() {
            return false;
        }

        @Override
        @CheckForNull
        public CalculatedMetricFormula getCalculatedMetricFormula() {
            return null;
        }
    }

}
