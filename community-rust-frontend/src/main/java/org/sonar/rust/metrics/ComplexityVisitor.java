/*
 * Community Rust Plugin
 * Copyright (C) 2021-2025 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
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
package org.sonar.rust.metrics;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustVisitor;

public class ComplexityVisitor extends RustVisitor {

  private int complexity;

  private static boolean isSomethingComplex(AstNode node) {
    // placeholder for ideas may have ideas later
    return node.getChildren().size() > 42;
  }

  public int complexity() {
    return complexity;
  }

  @Override
  public Set<AstNodeType> subscribedKinds() {
    return new HashSet<>(Arrays.asList(
      RustGrammar.STATEMENT));
  }

  @Override
  public void visitFile(AstNode astNode) {
    complexity = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isSomethingComplex(astNode)) {
      complexity++;
    }
  }

}
