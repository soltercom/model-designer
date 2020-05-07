package model.forms;

import formsfx.model.event.FieldEvent;
import formsfx.model.structure.Field;
import formsfx.model.structure.Form;
import formsfx.model.structure.Group;
import formsfx.model.structure.ModelField;
import formsfx.model.validator.RegexValidator;
import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.model.Model;

import java.util.Optional;

public class AttributeForm extends MetadataForm {

    public AttributeForm(Attribute metadata) {
        super(metadata);
    }

    @Override
    protected void createForm() {
        formInstance = Form.of(
            Group.of(
                Field.ofStringType(metadata.nameProperty())
                        .label("Имя")
                        .required("Не указано имя")
                        .validate(RegexValidator.forNames("Не верно задано имя")),
                Field.ofModelType(((Attribute)metadata).typeProperty())
                    .label("Тип")
                    .required("Не указан тип")
                    .addEventHandler(FieldEvent.EVENT_FIELD_START_SELECTION, this::onFieldStartSelection)
            )
        ).title("Метаданные");
    }

    private void onFieldStartSelection(FieldEvent event) {
        Metadata currentValue = ((ModelField) event.getField()).getValue();
        ModelSelectionForm modelSelectionForm = new ModelSelectionForm(currentValue);
        modelSelectionForm.open()
                .ifPresent(value -> ((ModelField) event.getField()).setSelectedValue((Model) value));
    }

}
