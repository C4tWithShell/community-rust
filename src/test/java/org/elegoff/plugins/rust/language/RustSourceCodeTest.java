/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.elegoff.plugins.rust.language;

import org.elegoff.plugins.rust.Utils;
import org.elegoff.plugins.rust.languages.RustSourceCode;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class RustSourceCodeTest {
    InputFile inputFile;
    RustSourceCode code;

    @Before
    public void setInputFile() throws IOException {
        inputFile = Utils.getInputFile("clippy/main.rs");
        code = new RustSourceCode(inputFile, Optional.of(Boolean.FALSE));
    }

    @Test
    public void testGetRustFile() throws IOException {
        assertEquals(inputFile, code.getRustFile());
        assertEquals("fn main() {\n" +
                "    println!(\"Checking issues\");\n" +
                "    absurd_extreme_comparison();\n" +
                "    println!(\"Done\");\n" +
                "}\n" +
                "\n" +
                "fn absurd_extreme_comparison(){\n" +
                "    let vec: Vec<isize> = Vec::new();\n" +
                "    if vec.len() <= 0 {}\n" +
                "    if 100 > std::i32::MAX {}\n" +
                "}", code.getContent());
    }

}
