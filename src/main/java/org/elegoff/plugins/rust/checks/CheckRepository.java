package org.elegoff.plugins.rust.checks;

import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class CheckRepository {
    public static final String REPOSITORY_KEY = "rust";
    public static final String REPOSITORY_NAME = "Rust Analyzer";

    private static final List<Class<? extends RustCheck>> CHECK_CLASSES = Arrays.asList(
            BracesCheck.class
        
        );

    private static final List<String> TEMPLATE_RULE_KEYS = Arrays.asList(
            
    );


    /**
     * Hide constructor
     */
    private CheckRepository() {
    }


    /**
     * Returns the rule key of the check {@link ParsingErrorCheck}
     *
     * @return the rule key of the check {@link ParsingErrorCheck}
     */
    public static Class<? extends RustCheck> getParsingErrorCheckClass() {
        return ParsingErrorCheck.class;
    }

    /**
     * Returns all non-syntactical check classes
     *
     * @return all check classes
     */
    public static List<Class<? extends RustCheck>> getCheckClasses() {
        return CHECK_CLASSES;
    }

    /**
     * Returns the keys of the rules that are parameterized, i.e. that are templates
     *
     * @return the keys of the rules that are parameterized, i.e. that are templates
     */
    public static List<String> getTemplateRuleKeys() {
        return TEMPLATE_RULE_KEYS;
    }
}
