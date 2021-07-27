package org.elegoff.rust.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustPunctuator;
import org.sonar.sslr.internal.matchers.AstCreator;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Rule(key = "EmptyEnum")
public class EmptyEnumCheck extends RustCheck {
    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Collections.singleton(RustGrammar.ENUMERATION);
    }

    @Override
    public void visitNode(AstNode node) {
        List<AstNode> test = node.getChildren();

        AstNode enumItems = node.getFirstChild(RustGrammar.ENUM_ITEMS);

        if (enumItems == null) {
            raiseIssue(node);
        }


    }

    private void raiseIssue(AstNode node) {
        addIssue("Either remove or fill this empty enumeration.", node);
    }

}


