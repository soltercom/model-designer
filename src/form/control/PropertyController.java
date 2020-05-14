package form.control;

import form.utils.ReflectionUtils;
import form.validator.Validator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PropertyController<T> {

    protected final Property<T> property;
    protected final Field field;
    protected Property<T> persistProperty;

    protected BooleanProperty changed = new SimpleBooleanProperty(false);
    protected final BooleanProperty valid = new SimpleBooleanProperty(true);

    protected final List<Validator<T>> validators = new ArrayList<>();

    public PropertyController(Field field, Property<T> property, Property<T> persistProperty) {
        this.property = property;
        this.field = field;
        this.persistProperty = persistProperty;
        setValidators();
        setBindings();
    }

    private void setValidators() {
        ReflectionUtils.getValidators(field)
            .forEach(v -> validators.add((Validator<T>)v));
        validate();
    }

    private void setBindings() {
        changed.bind(Bindings.createBooleanBinding(() -> !property.getValue().equals(persistProperty.getValue()), property, persistProperty));
    }

    public boolean validate() {
        boolean result = validators.stream()
                .map(v -> v.validate(property.getValue()))
                .allMatch(r -> r);

        valid.set(result);
        return result;
    }

    public void save() {
        persistProperty.setValue(property.getValue());
    }

    public void cancel() {
        property.setValue(persistProperty.getValue());
    }

    public boolean isChanged() {
        return changed.get();
    }
    public BooleanProperty changedProperty() {
        return changed;
    }
    public boolean isValid() {
        return valid.get();
    }
    public BooleanProperty validProperty() {
        return valid;
    }

    public Property<T> getProperty() {
        return property;
    }
    public Field getField() {
        return field;
    }

}
