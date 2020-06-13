package org.sonar.rust;

import org.sonar.plugins.rust.api.RustCheck;

public interface EndOfAnalysisCheck extends RustCheck {

    /**
     * Method called at the end of analysis, after all files have been scanned
     */
    void endOfAnalysis();

}
