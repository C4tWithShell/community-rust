package org.elegoff.rust.checks;

import java.util.*;

public class CheckList {
    public static final String REPOSITORY_KEY = "rust";

    private CheckList() {
    }

    public static Iterable<Class> getChecks() {
        List emptySoFar = new ArrayList();
        return Collections.unmodifiableSet(new HashSet<>(emptySoFar));
    }

}
