package org.sonar.rust.parser;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;

import static java.nio.charset.StandardCharsets.UTF_8;

public class RustParserTest {

    private final RustParser parser = RustParser.create();

    @Test
    public void test() throws Exception {
        Collection<File> files = listFiles();
        for (File file : files) {
            String fileContent = new String(Files.readAllBytes(file.toPath()), UTF_8);
            parser.parse(fileContent);
        }
    }

    private static Collection<File> listFiles() {
        File dir = new File("src/test/resources/parser/");
        return FileUtils.listFiles(dir, new String[]{"rs"}, true);
    }

}
