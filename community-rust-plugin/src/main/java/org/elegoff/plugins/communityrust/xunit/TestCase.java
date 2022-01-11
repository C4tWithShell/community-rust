package org.elegoff.plugins.communityrust.xunit;

import javax.annotation.Nullable;

/**
 * Represents a unit test case. Has a couple of data items like name, status, time etc. associated. Reports testcase details in
 * sonar-conform XML
 */
public class TestCase {
    private final String name;
    private final TestCaseStatus status;
    private final String stackTrace;
    private final String errorMessage;
    private final int time;
    private final String file;
    private final String testClassname;

    /**
     * Constructs a testcase instance out of following parameters
     * @param name  The name of this testcase
     * @param status The execution status of the testcase
     * @param stackTrace The stack trace occurred while executing of this testcase; pass "" if the testcase passed/skipped.
     * @param errorMessage The error message associated with this testcase of the execution was erroneous; pass "" if not.
     * @param time The execution time in milliseconds
     * @param file The optional file to which this test case applies.
     * @param testClassname The classname of the test.
     */
    public TestCase(String name, TestCaseStatus status, String stackTrace, String errorMessage, int time, @Nullable String file, @Nullable  String testClassname) {
        this.name = name;
        this.status = status;
        this.stackTrace = stackTrace;
        this.errorMessage = errorMessage;
        this.time = time;
        this.file = file;
        this.testClassname = testClassname;
    }
    /**
     * Returns true if this testcase is an error, false otherwise
     */
    public boolean isError(){
        return TestCaseStatus.ERROR.equals(status);
    }
    /**
     * Returns true if this testcase is a failure, false otherwise
     */
    public boolean isFailure(){
        return TestCaseStatus.FAILURE.equals(status);
    }

    /**
     * Returns true if this testcase has been skipped, failure, false otherwise
     */
    public boolean isSkipped() {
        return TestCaseStatus.SKIPPED.equals(status);
    }

    public int getTime() {
        return time;
    }

    public String getFile() {
        return file;
    }

    public String getTestClassname() {
        return testClassname;
    }
}
