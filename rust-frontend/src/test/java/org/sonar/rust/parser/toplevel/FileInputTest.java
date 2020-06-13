package org.sonar.rust.parser.toplevel;

import org.junit.Before;
import org.junit.Test;
import org.sonar.rust.api.RustGrammar;
import org.sonar.rust.parser.RuleTest;

import static org.sonar.rust.parser.RustParserAssert.assertThat;

public class FileInputTest extends RuleTest {

    @Before
    public void init() {
        setRootRule(RustGrammar.FILE_INPUT);
    }

    @Test
    public void ok() {
        assertThat(p)

                .matches("println!(\"Hello, world!\");")
                //.matches("print foo; print toto")
                .matches("\n");
                //.matches("print foo\nprint foo");
    }

}

