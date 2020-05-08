package test.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class DataControl<P extends Property> extends SimpleControl {

    protected P value;
    protected P persistValue;

    public DataControl(P value) {
        this.persistValue = value;
    }

    public void save() {
        persistValue.setValue(value.getValue());
    }

    public void cancel() {
        value.setValue(persistValue.getValue());
    }

    public SimpleControl required(String errorMessage) {
        required.set(true);

        validate();

        return this;
    }

    public boolean validateRequired(P newValue) {
        return !isRequired() || (isRequired() && !newValue.getValue().toString().isEmpty());
    }

    public boolean validate() {
        if (!validateRequired(value)) {
            valid.set(false);
            return false;
        }
        valid.set(true);
        return true;
    }

    @Override
    public void initBindings() {
        super.initBindings();
    }

}
