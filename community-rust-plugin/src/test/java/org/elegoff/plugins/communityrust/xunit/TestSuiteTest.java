package org.elegoff.plugins.communityrust.xunit;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TestSuiteTest {

    @Test
    public void test() {
        TestSuite suite = new TestSuite("key");
        assertThat(suite.getKey()).isEqualTo("key");
        assertThat(suite.getTestCases()).isEmpty();

        TestCase testCase = new TestCase("name", TestCaseStatus.OK, "stack", "msg", 1, "file", "testClassname");
        suite.addTestCase(testCase);
        assertThat(suite.getTestCases()).containsExactly(testCase);
    }

}