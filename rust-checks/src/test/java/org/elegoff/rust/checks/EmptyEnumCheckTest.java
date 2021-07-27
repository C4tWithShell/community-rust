package org.elegoff.rust.checks;

import org.junit.Test;

import java.io.File;

public class EmptyEnumCheckTest {

    @Test
    public void test() {
        RustCheckVerifier.verify(new File("src/test/resources/checks/empty_enum.rs"), new EmptyEnumCheck());
    }
}
