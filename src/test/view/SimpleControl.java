package test.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;


public abstract class SimpleControl implements ViewInit {

    final protected static double SPACING = 10.0;

    protected final StringProperty label = new SimpleStringProperty("");

    protected final BooleanProperty changed = new SimpleBooleanProperty(false);
    protected final BooleanProperty required = new SimpleBooleanProperty(false);
    protected final BooleanProperty valid = new SimpleBooleanProperty(true);

    protected Label labelControl;

    public SimpleControl() {
    }

    @Override
    public void initNodes() {
        labelControl = new Label();
    }

    @Override
    public void initBindings() {
        labelControl.textProperty().bind(label);
    }

    public SimpleControl label(String label) {
        this.label.set(label);
        return this;
    }

    public static StringControl getStringControl(StringProperty stringProperty) {
        return new StringControl(stringProperty);
    }

    public abstract Pane getContainer();
    public abstract void save();
    public abstract void cancel();

    public boolean hasChanged() {
        return changed.get();
    }
    public BooleanProperty changedProperty() {
        return changed;
    }
    public boolean isRequired() {
        return required.get();
    }
    public BooleanProperty requiredProperty() {
        return required;
    }
    public boolean isValid() {
        return valid.get();
    }
    public BooleanProperty validProperty() {
        return valid;
    }

}
