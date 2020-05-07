package formsfx.model.structure;

import formsfx.model.event.FieldEvent;
import formsfx.model.validator.ValidationResult;
import formsfx.model.validator.Validator;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.sun.javafx.event.EventUtil.fireEvent;

public abstract class DataField<P extends Property, V, F extends Field<F>> extends Field<F> implements FormElement {

    protected final P value;
    protected final P persistentValue;
    protected final StringProperty userInput = new SimpleStringProperty("");

    protected final StringProperty formatError = new SimpleStringProperty("");

    protected final List<Validator<V>> validators = new ArrayList<>();

    protected static abstract class AbstractStringConverter<V> extends StringConverter<V> {
        @Override
        public String toString(V object) {
            return String.valueOf(object);
        }
    }

    protected StringConverter<V> stringConverter = new AbstractStringConverter<V>() {
        @Override
        public V fromString(String string) {
            return null;
        }
    };

    private final InvalidationListener externalBindingListener = (observable) -> userInput.setValue(stringConverter.toString((V) ((P) observable).getValue()));

    protected boolean validateRequired(String newValue) {
        return !isRequired() || (isRequired() && !newValue.isEmpty());
    }

    public boolean validate() {
        String newValue = userInput.getValue();

        if (!validateRequired(newValue)) {
            if (!requiredError.get().isEmpty()) {
                errorMessages.setAll(requiredError.get());
            }

            valid.set(false);
            return false;
        }

        V transformedValue;

        try {
            transformedValue = stringConverter.fromString(newValue);
        } catch (Exception e) {
            if (!formatError.get().isEmpty()) {
                errorMessages.setAll(formatError.get());
            }
            valid.set(false);
            return false;
        }

        List<String> errorMessages = validators.stream()
                .map(v -> v.validate(transformedValue))
                .filter(r -> !r.getResult())
                .map(ValidationResult::getErrorMessage)
                .collect(Collectors.toList());

        this.errorMessages.setAll(errorMessages);

        if (errorMessages.size() > 0) {
            valid.set(false);
            return false;
        }

        valid.set(true);
        return true;
    }

    protected DataField(P valueProperty, P persistentValueProperty) {
        value = valueProperty;
        persistentValue = persistentValueProperty;

        changed.bind(Bindings.createBooleanBinding(() -> !stringConverter.toString((V) persistentValue.getValue()).equals(userInput.getValue()), userInput, persistentValue));

        userInput.addListener((observable, oldValue, newValue) -> {
            if (validate()) {
                value.setValue(stringConverter.fromString(newValue));
            }
        });

    }

    public F format(StringConverter<V> newValue) {
        stringConverter = newValue;
        validate();
        return (F) this;
    }

    public F format(StringConverter<V> newValue, String errorMessage) {
        stringConverter = newValue;
        formatError.set(errorMessage);
        validate();
        return (F) this;
    }

    public F format(String errorMessage) {
        formatError.set(errorMessage);
        validate();
        return (F) this;
    }

    public F bind(P binding) {
        persistentValue.bindBidirectional(binding);
        binding.addListener(externalBindingListener);

        return (F) this;
    }

    public F unbind(P binding) {
        persistentValue.unbindBidirectional(binding);
        binding.removeListener(externalBindingListener);

        return (F) this;
    }

    public void persist() {

        persistentValue.setValue(value.getValue());

        fireEvent(FieldEvent.fieldPersistedEvent(this));
    }

    public void reset() {
        if (!hasChanged()) {
            return;
        }

        userInput.setValue(stringConverter.toString((V) persistentValue.getValue()));
    }

    public String getUserInput() {
        return userInput.get();
    }

    public StringProperty userInputProperty() {
        return userInput;
    }

    public V getValue() {
        return (V) value.getValue();
    }

    public P valueProperty() {
        return value;
    }

    @SafeVarargs
    public final F validate(Validator<V>... newValue) {
        validators.clear();
        Collections.addAll(validators, newValue);
        validate();

        return (F) this;
    }


}
