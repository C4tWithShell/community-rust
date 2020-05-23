package org.elegoff.plugins.rust.clippy;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Linter{

/**
     * Rule token
     */
    private static final String RULE_TOKEN = "rule:";

    /**
     * Key for error levels
     */
    public static final String LEVEL_KEY = "level";

    /**
     * Error level for when there is no error...
     */
    public static final String NONE_LEVEL = "none";

    /**
     * Info error level
     */
    public static final String INFO_LEVEL = "info";

    /**
     * Warning error level
     */
    public static final String WARNING_LEVEL = "warning";

    /**
     * Highest error level
     */
    public static final String ERROR_LEVEL = "error";


    /**
     * Map used to resolve the error levels by number or ID
     */
    private static final Map<Object, Object> PROBLEM_LEVELS = Collections.unmodifiableMap(
            Stream.of(
                    new AbstractMap.SimpleEntry<>(0, NONE_LEVEL),
                    new AbstractMap.SimpleEntry<>(NONE_LEVEL, 0),
                    new AbstractMap.SimpleEntry<>(1, INFO_LEVEL),
                    new AbstractMap.SimpleEntry<>(INFO_LEVEL, 1),
                    new AbstractMap.SimpleEntry<>(2, WARNING_LEVEL),
                    new AbstractMap.SimpleEntry<>(WARNING_LEVEL, 2),
                    new AbstractMap.SimpleEntry<>(3, ERROR_LEVEL),
                    new AbstractMap.SimpleEntry<>(ERROR_LEVEL, 3)
            ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
    );


    /**
     * Hide constructor
     */
    private Linter() {
    }

    /**
     * Parses the passed RUST string to detect syntax errors. If an error is met, a problem is return.
     *
     * @param buffer a RUST string
     * @return a problem or <code>null</code> if there is no syntax error
     */
    public static LintProblem getSyntaxError(String buffer) {
        //TODO
        return null;
    }

	public static List<LintProblem> getCosmeticProblems(String content, ClippyConfig clippyConfig, Object object) {
		return null;
	}

}