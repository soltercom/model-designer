package formsfx.model.structure;

import formsfx.model.event.FieldEvent;
import formsfx.view.controls.SimpleModelControl;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.core.factory.MetadataFactory;
import model.core.model.Model;

import java.time.LocalDate;

public class ModelField extends DataField<ObjectProperty<Model>, Model, ModelField> {

    public ModelField(ObjectProperty<Model> valueProperty, ObjectProperty<Model> persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<Model>() {
            @Override
            public Model fromString(String s) {
                return (Model)MetadataFactory.getInstance().getRootNode().getProperty(s);
            }
        };
        renderer = new SimpleModelControl();
        userInput.setValue(null);
        userInput.setValue(stringConverter.toString((Model) persistentValue.getValue()));

    }

    @Override
    public ModelField bind(ObjectProperty<Model> binding) {
        return super.bind(binding);
    }

    public void fireStartSelectionEvent() {
        fireEvent(FieldEvent.fieldStartSelection(this));
    }

    public void setSelectedValue(Model model) {
        userInput.setValue(model.toString());
    }
}
