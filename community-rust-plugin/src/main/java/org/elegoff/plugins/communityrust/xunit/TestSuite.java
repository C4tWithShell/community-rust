package org.elegoff.plugins.communityrust.xunit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a unit test suite. Contains testcases, maintains some statistics. Reports testcase details in sonar-conform XML
 */
public class TestSuite {

    private String key;
    private List<TestCase> testCases;

    /**
     * Creates a testsuite instance uniquely identified by the given key
     *
     * @param key
     *          The key to construct a testsuite for
     */
    public TestSuite(String key) {
        this.key = key;
        this.testCases = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void addTestCase(TestCase tc) {
        testCases.add(tc);
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

//    /**
//     * Returns execution details as sonar-conform XML
//     */
//    public String getDetails() {
//        StringBuilder details = new StringBuilder();
//        details.append("<tests-details>");
//        for (TestCase tc : testCases) {
//            details.append(tc.getDetails());
//        }
//        details.append("</tests-details>");
//        return details.toString();
//    }

}