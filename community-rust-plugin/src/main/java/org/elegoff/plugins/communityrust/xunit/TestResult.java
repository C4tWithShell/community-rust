package org.elegoff.plugins.communityrust.xunit;

public class TestResult {
    private int errors = 0;
    private int skipped = 0;
    private int tests = 0;
    private int time = 0;
    private int failures = 0;


    public int getErrors() {
        return errors;
    }

    public int getSkipped() {
        return skipped;
    }

    public int getTests() {
        return tests;
    }

    public int getExecutedTests() {
        return tests - skipped;
    }

    public int getTime() {
        return time;
    }

    public int getFailures() {
        return failures;
    }

    public void addTestCase(TestCase tc) {
        if (tc.isSkipped()) {
            skipped++;
        } else if (tc.isFailure()) {
            failures++;
        } else if (tc.isError()) {
            errors++;
        }
        tests++;
        time += tc.getTime();
    }
}
