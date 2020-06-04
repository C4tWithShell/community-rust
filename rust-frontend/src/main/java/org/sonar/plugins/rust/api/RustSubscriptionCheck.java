package org.sonar.plugins.rust.api;


public abstract class RustSubscriptionCheck implements SubscriptionCheck, RustCheck {
    @Override
    public void scanFile(RustVisitorContext visitorContext) {
        // empty implementation
    }

    public void leaveFile() {
        // callback when leaving file
    }
}