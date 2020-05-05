package model.core.model;

import model.core.Metadata;

public abstract class PredefinedModel extends Model {
    public PredefinedModel(String name) {
        super(name, true);
    }

    public boolean add(Metadata property) { return false; }
    public boolean remove(Metadata property) { return false; }
}
