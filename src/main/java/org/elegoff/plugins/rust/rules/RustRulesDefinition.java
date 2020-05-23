package org.elegoff.plugins.rust.rules;

import org.elegoff.plugins.rust.checks.CheckRepository;
import org.elegoff.plugins.rust.languages.RustLanguage;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;



import java.util.ArrayList;
import java.util.List;

/**
 * Rules definition class for this plugin
 */
public class RustRulesDefinition implements RulesDefinition {
    /**
     * Path to the directory/folder containing the descriptor files (JSON and HTML) for the rules
     */
    public static final String RULES_DEFINITION_FOLDER = "org/sonar/l10n/rust/rules/rust";


    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(CheckRepository.REPOSITORY_KEY, RustLanguage.KEY).setName(CheckRepository.REPOSITORY_NAME);

        RuleMetadataLoader metadataLoader = new RuleMetadataLoader(RULES_DEFINITION_FOLDER);
        @SuppressWarnings("rawtypes")
        List<Class> allCheckClasses = new ArrayList<>(CheckRepository.getCheckClasses());
        metadataLoader.addRulesByAnnotatedClass(repository, allCheckClasses);

        // Declare rule templates
        for (NewRule rule : repository.rules()) {
            if (CheckRepository.getTemplateRuleKeys().contains(rule.key())) {
                rule.setTemplate(true);
            }
        }

        repository.done();
    }
}