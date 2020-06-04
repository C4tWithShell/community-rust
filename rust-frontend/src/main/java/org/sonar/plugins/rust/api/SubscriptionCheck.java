package org.sonar.plugins.rust.api;

import org.sonar.plugins.rust.api.tree.Tree;

import java.util.function.Consumer;

public interface SubscriptionCheck {
    void initialize(Context context);

    interface Context {

        void registerSyntaxNodeConsumer(Tree.Kind elementType, Consumer<SubscriptionContext> consumer);

    }
}
