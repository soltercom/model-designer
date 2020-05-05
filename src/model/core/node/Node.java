package model.core.node;

import model.core.Metadata;

public abstract class Node extends Metadata {
    public Node(String name) {
        super(name, true);
    }

    @Override
    public String toString() {
        return getName();
    }
}
