package org.elegoff.plugins.communityrust.xunit;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestResultTest {

  TestResult testResult;

  @Before
  public void setUp() {
    testResult = new TestResult();
  }

  @Test
  public void newBornSuiteShouldHaveVirginStatistics() {
    assertThat(testResult.getTests()).isEqualTo(0);
    assertThat(testResult.getExecutedTests()).isEqualTo(0);
    assertThat(testResult.getErrors()).isEqualTo(0);
    assertThat(testResult.getFailures()).isEqualTo(0);
    assertThat(testResult.getSkipped()).isEqualTo(0);
    assertThat(testResult.getTime()).isEqualTo(0);
  }

  @Test
  public void addingTestCaseShouldIncrementStatistics() {
    int testBefore = testResult.getTests();
    int timeBefore = testResult.getTime();

    final int EXEC_TIME = 10;
    testResult.addTestCase(createTestCase(EXEC_TIME, "ok"));

    assertThat(testResult.getTests()).isEqualTo(testBefore + 1);
    assertThat(testResult.getTime()).isEqualTo(timeBefore + EXEC_TIME);
  }

  @Test
  public void executedTestsValue() {
    testResult.addTestCase(createTestCase(1, "ok"));
    testResult.addTestCase(createTestCase(2, "skipped"));
    testResult.addTestCase(createTestCase(3, "ok"));
    testResult.addTestCase(createTestCase(4, "error"));
    testResult.addTestCase(createTestCase(5, "skipped"));

    assertThat(testResult.getTests()).isEqualTo(5);
    assertThat(testResult.getExecutedTests()).isEqualTo(3);
    assertThat(testResult.getErrors()).isEqualTo(1);
    assertThat(testResult.getFailures()).isEqualTo(0);
    assertThat(testResult.getSkipped()).isEqualTo(2);
    assertThat(testResult.getTime()).isEqualTo(15);
  }

  private static TestCase createTestCase(int time, String status) {
    return new TestCase("name", TestCaseStatus.getFromIgnoreCaseString(status), "stack", "msg", time,"file", "testClassname");
  }

  @Test
  public void addingAnErroneousTestCaseShouldIncrementErrorStatistic() {
    int errorsBefore = testResult.getErrors();
    TestCase error = mock(TestCase.class);
    when(error.isError()).thenReturn(true);

    testResult.addTestCase(error);

    assertThat(testResult.getErrors()).isEqualTo(errorsBefore + 1);
  }

  @Test
  public void addingAFailedestCaseShouldIncrementFailedStatistic() {
    int failedBefore = testResult.getFailures();
    TestCase failedTC = mock(TestCase.class);
    when(failedTC.isFailure()).thenReturn(true);

    testResult.addTestCase(failedTC);

    assertThat(testResult.getFailures()).isEqualTo(failedBefore + 1);
  }

  @Test
  public void addingASkippedTestCaseShouldIncrementSkippedStatistic() {
    int skippedBefore = testResult.getSkipped();
    TestCase skippedTC = mock(TestCase.class);
    when(skippedTC.isSkipped()).thenReturn(true);

    testResult.addTestCase(skippedTC);

    assertThat(testResult.getSkipped()).isEqualTo(skippedBefore + 1);
  }

}