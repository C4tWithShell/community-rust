/**
 * Community Rust Plugin
 * Copyright (C) 2021-2022 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.rust.checks;

import org.junit.Test;

import java.io.File;

public class FunctionParametersCountCheckTest {
    @Test
    public void test() {
        RustCheckVerifier.verify(new File("src/test/resources/checks/function_params_count.rs"), new FunctionParametersCountCheck());
    }

    @Test
    public void custom() {
        FunctionParametersCountCheck check = new FunctionParametersCountCheck();
        check.maximumParameterCount = 10;

        RustCheckVerifier.verifyNoIssueIgnoringExpected(new File("src/test/resources/checks/function_params_count.rs"), check);
    }
}
