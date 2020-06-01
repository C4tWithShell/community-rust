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
