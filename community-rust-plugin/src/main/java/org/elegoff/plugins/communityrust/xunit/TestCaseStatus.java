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
package org.elegoff.plugins.communityrust.xunit;

public enum TestCaseStatus {
  OK("ok"),
  ERROR("error"),
  FAILURE("failure"),
  SKIPPED("skipped");

  private final String text;

  TestCaseStatus(String name) {
    this.text = name;
  }

  public static TestCaseStatus getFromIgnoreCaseString(String value) {
    return TestCaseStatus.valueOf(value.toUpperCase());
  }

  @Override
  public String toString() {
    return text;
  }
}
