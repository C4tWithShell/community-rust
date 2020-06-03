package org.sonar.rust.parser;

import com.sonar.sslr.api.AstNode;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class RustParserTest {

    private final RustParser parser = RustParser.create();

    @Test
    public void testParse() throws Exception {
        Collection<File> files = listFiles();
        AstNode node;
        for (File file : files) {
            String fileContent = new String(Files.readAllBytes(file.toPath()), UTF_8);
            node =parser.parse(fileContent);
            assertThat(node).isNotNull();
        }
    }

    private static Collection<File> listFiles() {
        File dir = new File("src/test/resources/parser/");
        return FileUtils.listFiles(dir, new String[]{"rs"}, true);
    }

}
