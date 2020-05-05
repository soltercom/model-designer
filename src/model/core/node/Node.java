package model.core.node;

import model.core.Metadata;

public abstract class Node extends Metadata {
    public Node(Metadata parent, String name) {
        super(parent, name, true);
    }

    @Override
    public String toString() {
        return getName();
    }
}
