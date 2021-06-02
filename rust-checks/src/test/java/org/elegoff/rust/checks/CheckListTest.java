package org.elegoff.rust.checks;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckListTest {


    @Test
    public void testSize(){
        assertThat(CheckList.getRustChecks().isEmpty());
    }
}
