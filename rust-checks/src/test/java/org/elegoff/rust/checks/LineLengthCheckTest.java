package org.elegoff.rust.checks;

import org.junit.Test;

import java.io.File;

public class LineLengthCheckTest {
    @Test
    public void test() {
        RustCheckVerifier.verify(new File("src/test/resources/checks/lineLength.rs"), new LineLengthCheck());
    }

    @Test
    public void custom() {
        LineLengthCheck check = new LineLengthCheck();
        check.maximumLineLength = 119;
        RustCheckVerifier.verify(new File("src/test/resources/checks/lineLength119.rs"), check);
    }
}
