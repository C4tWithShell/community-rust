package org.elegoff.plugins.rust.clippy;

import java.util.Map;

public class ClippyConfig {

    protected Map<String, Object> ruleConf;

    public ClippyConfig(String content) throws ClippyConfigException {
        if (content != null) {
            parse(content);
            //TODO validate();
        } else {
            throw new IllegalArgumentException("content cannot be null");
        }
    }

    protected void parse(String rawContent) throws ClippyConfigException {
       
//TODO
};


protected void validate() throws ClippyConfigException {
    for (Map.Entry<String, Object> entry : ruleConf.entrySet()) {
        String id = entry.getKey();

        /*
        Rule rule = RuleFactory.instance.getRule(id);
        if (rule == null) {
            throw new ClippyConfigException("invalid config: no such rule: \"" + id + "\"");
        }

        Map<String, Object> newConf = validateRuleConf(rule, entry.getValue());
        ruleConf.put(id, newConf);

     */   
    }
}

}
