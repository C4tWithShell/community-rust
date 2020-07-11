package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class BlockExpressionTest {

    /*
    BlockExpression :
   {
      InnerAttribute*
      Statements?
   }

Statements :
      Statement+
   | Statement+ ExpressionWithoutBlock
   | ExpressionWithoutBlock
     */


    @Test
    public void testBlockExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_EXPRESSION))
                .matches("{let y=42;}")
        ;
    }
}
