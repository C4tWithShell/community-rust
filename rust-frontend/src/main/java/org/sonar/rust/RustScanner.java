/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust;

import com.google.common.collect.Iterables;
import com.sonar.sslr.api.typed.ActionParser;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import org.sonar.plugins.rust.api.RustCheck;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.ast.RustAstScanner;
import org.sonar.rust.ast.visitors.SyntaxHighlighterVisitor;
import org.sonar.rust.ast.visitors.FileLinesVisitor;
import org.sonar.rust.ast.parser.RustParser;
import org.sonar.rust.model.VisitorsBridge;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RustScanner {
    private static final Logger LOG = Loggers.get(RustScanner.class);

    private final RustAstScanner astScanner;

    public RustScanner(SonarComponents sonarComponents, Measurer measurer, RustCheck... visitors) {


        Iterable<RustCheck> codeVisitors = Iterables.concat(new ArrayList(), Arrays.asList(visitors));



        if (sonarComponents != null) {
            codeVisitors =  Iterables.concat(codeVisitors, Arrays.asList(new FileLinesVisitor(sonarComponents), new SyntaxHighlighterVisitor(sonarComponents)));
        }

        //AstScanner for main files
        ActionParser<Tree> parser = RustParser.createParser();
        astScanner = new RustAstScanner(parser, sonarComponents);
        astScanner.setVisitorBridge(createVisitorBridge(codeVisitors,sonarComponents));

    }


    public void scan(Iterable<InputFile> sourceFiles) {
        Profiler profiler = Profiler.create(LOG).startInfo("Rust Files AST scan");
        astScanner.scan(sourceFiles);
        profiler.stopInfo();
    }

    private static VisitorsBridge createVisitorBridge(Iterable visitors, @Nullable SonarComponents sonarComponents) {
        VisitorsBridge visitorsBridge = new VisitorsBridge(visitors, sonarComponents);

        return visitorsBridge;
    }
}
