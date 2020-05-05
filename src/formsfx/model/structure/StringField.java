package formsfx.model.structure;

import formsfx.view.controls.SimpleTextControl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringField extends DataField<StringProperty, String, StringField> {

    protected StringField(SimpleStringProperty valueProperty, SimpleStringProperty persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<String>() {
            @Override
            public String fromString(String string) {
                return string;
            }
        };
        renderer = new SimpleTextControl();

        userInput.set(stringConverter.toString(value.getValue()));
    }

}
