package org.sonar.rust.model;

import org.sonar.plugins.rust.api.RustCheck;
import org.sonar.rust.SonarComponents;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class VisitorsBridge {
    private final SonarComponents sonarComponents;
    public VisitorsBridge( SonarComponents sonarComponents) {
        this.sonarComponents = sonarComponents;
    }
}
