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
package org.elegoff.plugins.rust.api;



import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import org.elegoff.plugins.rust.api.tree.Token;
import org.elegoff.plugins.rust.api.tree.Tree;

public interface RustCheck {
    void scanFile(RustVisitorContext visitorContext);

    class PreciseIssue {

        private final RustCheck check;
        private final IssueLocation primaryLocation;
        private Integer cost;
        private final List<IssueLocation> secondaryLocations;

        public PreciseIssue(RustCheck check, IssueLocation primaryLocation) {
            this.check = check;
            this.primaryLocation = primaryLocation;
            this.secondaryLocations = new ArrayList<>();
        }

        @Nullable
        public Integer cost() {
            return cost;
        }

        public PreciseIssue withCost(int cost) {
            this.cost = cost;
            return this;
        }

        public IssueLocation primaryLocation() {
            return primaryLocation;
        }

        public PreciseIssue secondary(Tree tree, @Nullable String message) {
            secondaryLocations.add(IssueLocation.preciseLocation(tree, message));
            return this;
        }

        public PreciseIssue secondary(Token token, @Nullable String message) {
            secondaryLocations.add(IssueLocation.preciseLocation(token, message));
            return this;
        }

        public PreciseIssue secondary(IssueLocation issueLocation) {
            secondaryLocations.add(issueLocation);
            return this;
        }

        public PreciseIssue secondary(LocationInFile locationInFile, @Nullable String message) {
            secondaryLocations.add(IssueLocation.preciseLocation(locationInFile, message));
            return this;
        }

        public List<IssueLocation> secondaryLocations() {
            return secondaryLocations;
        }

        public RustCheck check() {
            return check;
        }
    }
}
