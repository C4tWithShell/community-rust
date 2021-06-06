package org.elegoff.plugins.rust.coverage.lcov;

import org.sonar.api.batch.fs.InputFile;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReversePathTree {
    private Node root = new Node();

    void index(InputFile inputFile, String[] path) {
        Node currentNode = root;
        for (int i = path.length - 1; i >= 0; i--) {
            currentNode = currentNode.children.computeIfAbsent(path[i], e -> new Node());
        }
        currentNode.file = inputFile;
    }

    InputFile getFileWithSuffix(String[] path) {
        Node currentNode = root;

        for (int i = path.length - 1; i >= 0; i--) {
            currentNode = currentNode.children.get(path[i]);
            if (currentNode == null) {
                return null;
            }
        }
        return getFirstLeaf(currentNode);
    }

    private static InputFile getFirstLeaf(Node node) {
        while (!node.children.isEmpty()) {
            node = node.children.values().iterator().next();
        }
        return node.file;
    }

    static class Node {
        final Map<String, Node> children = new LinkedHashMap<>();
        InputFile file = null;
    }
}
