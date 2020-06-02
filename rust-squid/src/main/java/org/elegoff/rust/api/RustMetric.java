package org.elegoff.rust.api;

import org.sonar.squidbridge.measures.CalculatedMetricFormula;
import org.sonar.squidbridge.measures.MetricDef;

import javax.annotation.CheckForNull;

public enum RustMetric implements MetricDef{
    FILES,
    LINES,
    LINES_OF_CODE,
    LINES_OF_CODE_IN_FUNCTION_BODY,
    STATEMENTS,
    FUNCTIONS,
    CLASSES,
    COMPLEXITY,
    COGNITIVE_COMPLEXITY,
    COMMENT_LINES,
    PUBLIC_API,
    PUBLIC_UNDOCUMENTED_API,
    COMPLEX_FUNCTIONS,
    COMPLEX_FUNCTIONS_LOC,
    LOC_IN_FUNCTIONS,
    BIG_FUNCTIONS,
    BIG_FUNCTIONS_LOC,
    NCLOC_DATA,
    EXECUTABLE_LINES_DATA,
    CPD_TOKENS_DATA,
    HIGHLIGTHING_DATA;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean isCalculatedMetric() {
        return false;
    }

    @Override
    public boolean aggregateIfThereIsAlreadyAValue() {
        return true;
    }

    @Override
    public boolean isThereAggregationFormula() {
        return true;
    }

    @Override
    @CheckForNull
    public CalculatedMetricFormula getCalculatedMetricFormula() {
        return null;
    }
}
