package org.elegoff.rust.checks;

import org.junit.Test;

import java.io.File;

public class FunctionParametersCountCheckTest {
    @Test
    public void test() {
        RustCheckVerifier.verify(new File("src/test/resources/checks/function_params_count.rs"), new FunctionParametersCountCheck());
    }

    @Test
    public void custom() {
        FunctionParametersCountCheck check = new FunctionParametersCountCheck();
        check.maximumParameterCount = 10;

        RustCheckVerifier.verifyNoIssueIgnoringExpected(new File("src/test/resources/checks/function_params_count.rs"), check);
    }
}
