package formsfx.model.structure;

import javafx.scene.Node;

public class NodeElement<N extends Node> extends Element {
    protected N node;

    public static <T extends Node> NodeElement<T> of(T node) {
        return new NodeElement(node);
    }

    protected NodeElement(N node) {
        if (node == null) {
            throw new NullPointerException("Node argument must not be null");
        }
        this.node = node;
    }

    public N getNode() {
        return node;
    }
}
