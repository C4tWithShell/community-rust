package org.sonar.rust.ast;

import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.model.VisitorsBridge;

import javax.annotation.Nullable;

public class RustAstScanner {
    private static final Logger LOG = Loggers.get(RustAstScanner.class);

    private final ActionParser<Tree> parser;
    private final SonarComponents sonarComponents;
    private VisitorsBridge visitor;

    public RustAstScanner(ActionParser<Tree> parser, @Nullable SonarComponents sonarComponents) {
        this.parser = parser;
        this.sonarComponents = sonarComponents;
    }

    public void setVisitorBridge(VisitorsBridge visitorBridge) {
        this.visitor=visitorBridge;
    }
}
