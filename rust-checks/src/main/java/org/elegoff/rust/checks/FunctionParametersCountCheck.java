package org.elegoff.rust.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.rust.RustGrammar;

import java.util.Collections;
import java.util.Set;

@Rule(key = "FunctionParametersCount")
public class FunctionParametersCountCheck extends RustCheck {

    private static final int DEFAULT_MAXIMUM_PARAMETER_COUNT = 8;

    @RuleProperty(
            key = "maximumParameterCount",
            description = " Maximum authorized number of parameters",
            defaultValue = "" + DEFAULT_MAXIMUM_PARAMETER_COUNT)
    public int maximumParameterCount = DEFAULT_MAXIMUM_PARAMETER_COUNT;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Collections.singleton(RustGrammar.FUNCTION);
    }

    @Override
    public void visitNode(AstNode node) {
        int numberOfParameters = getNumberOfParameters(node);

        if (numberOfParameters > maximumParameterCount) {
            addIssue(
                    "Reduce the number of parameters that this function takes from " + numberOfParameters + " to at most " + maximumParameterCount + ".",
                    node);
        }
    }

    private static int getNumberOfParameters(AstNode node) {
        AstNode parameterNameList = node.getFirstChild(RustGrammar.FUNCTION_PARAMETERS);

        return parameterNameList == null ? 0 : parameterNameList.getChildren(RustGrammar.FUNCTION_PARAM).size();
    }

}
