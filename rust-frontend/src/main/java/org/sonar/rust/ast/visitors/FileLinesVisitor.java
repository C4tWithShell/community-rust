/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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
package org.sonar.rust.ast.visitors;

import com.google.common.collect.ImmutableList;
import org.sonar.plugins.rust.api.tree.Tree;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.SubscriptionVisitor;
import java.util.*;

import static org.sonar.plugins.rust.api.tree.Tree.Kind.TOKEN;

public class FileLinesVisitor extends SubscriptionVisitor {

    public FileLinesVisitor(SonarComponents sonarComponents) {
        super();
        //TODO
    }



        @Override
        public List<Tree.Kind> nodesToVisit() {
            return ImmutableList.of(TOKEN
                   // ,METHOD, CONSTRUCTOR,
                    //INITIALIZER, STATIC_INITIALIZER,
                    //VARIABLE,
                    //FOR_EACH_STATEMENT, FOR_STATEMENT, WHILE_STATEMENT, DO_STATEMENT,
                    //LAMBDA_EXPRESSION
                    );

        }

}
