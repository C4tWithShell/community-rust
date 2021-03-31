package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class IfExpressionTest {

    @Test
    public void tesIfExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IF_EXPRESSION))
                .matches("if x == 4 {\n" +
                        "    println!(\"x is four\");\n" +
                        "} else if x == 3 {\n" +
                        "    println!(\"x is three\");\n" +
                        "} else {\n" +
                        "    println!(\"x is something else\");\n" +
                        "}")
                .matches("if run_coverage {\n" +
                        "        println!(\"Coverage is running\");" +
                        " } ")
                .matches("if bytes.len() < 3 * 4 {\n" +
                        "        println!(\"Too short\");" +
                        "        }")
                .matches("if bytes.len() < 3 * 4 {\n" +
                        "            return None;\n" +
                        "        }")
                .matches("if cfg!(target_os = \"windows\") {\n" +
                        "            PathBuf::from(\"/deni_dir/\")\n" +
                        "        } else {\n" +
                        "            PathBuf::from(\"/deno_dir/\")\n" +
                        "        }")
                .matches("if cfg!(target_os = \"windows\") {\n" +
                        "                    if let Some(Component::Prefix(prefix_component)) =\n" +
                        "                    path_components.next()\n" +
                        "                    {\n" +
                        "                        match prefix_component.kind() {\n" +
                        "                            Prefix::Disk(disk_byte) | Prefix::VerbatimDisk(disk_byte) => {\n" +
                        "                                let disk = 43;\n" +
                        "                            }\n" +
                        "                            Prefix::UNC(server, share)\n" +
                        "                            | Prefix::VerbatimUNC(server, share) => {\n" +
                        "\n" +
                        "                                let host = 44;\n" +
                        "\n" +
                        "                            }\n" +
                        "                            _ => unreachable!(),\n" +
                        "                        }\n" +
                        "                    }\n" +
                        "                }")


        ;
    }

    @Test
    public void tesIfLetExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IF_LET_EXPRESSION))
                .matches("if let (\"Bacon\",b) = dish {\n" +
                        "    println!(\"Bacon is served with {}\", b);\n" +
                        "} else {\n" +
                        "    // This block is evaluated instead.\n" +
                        "    println!(\"No bacon will be served\");\n" +
                        "}")


        ;
    }
}