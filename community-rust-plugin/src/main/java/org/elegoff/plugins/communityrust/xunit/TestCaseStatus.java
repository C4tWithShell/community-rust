package org.elegoff.plugins.communityrust.xunit;

public enum TestCaseStatus {
    OK("ok"),
    ERROR("error"),
    FAILURE("failure"),
    SKIPPED("skipped");
    private final String text;

    TestCaseStatus(String name) {
        this.text = name;
    }

    @Override
    public String toString() {
        return text;
    }

    public static TestCaseStatus getFromIgnoreCaseString(String value) {
        return TestCaseStatus.valueOf(value.toUpperCase());
    }
}

