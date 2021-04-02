package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ClosureExpressionTest {


    @Test
    public void testClosureParameters() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CLOSURE_PARAMETERS))
                .matches("k:i32")
                .matches("j")
                .matches("state: Rc<RefCell<OpState>>, bufs: BufVec")
        ;
    }

    @Test
    public void testClosureExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CLOSURE_EXPRESSION))
                .matches("|k:i32|->(){println!(\"hello,{}\",k)}")
                .matches("|j: i32| -> () { println!(\"hello, {}\", j); }")
                .matches("move |j| println!(\"{}, {}\", word, j)")
                .matches("move |state: Rc<RefCell<OpState>>, bufs: BufVec| -> Op {\n" +
                        "        let mut bufs_iter = bufs.into_iter();\n" +
                        "}")
                .matches("move |state : Rc<RefCell<OpState>>, bufs: BufVec| -> Op {\n" +
                        "        let mut b = 42;\n" +
                        "    }")
                .matches("|paths: Vec<PathBuf>| {\n" +
                        "        let config = get_typescript_config();\n" +
                        "        a\n" +
                        "            .boxed_local()\n" +
                        "    }")

            ;
    }
}
