package org.sonar.rust;

public class IllegalRuleParameterException extends IllegalArgumentException {
    public IllegalRuleParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
