package model.forms;

import formsfx.model.structure.Field;
import formsfx.model.structure.Form;
import formsfx.model.structure.Group;
import formsfx.model.validator.RegexValidator;
import model.core.Metadata;

public class RootNodeForm extends MetadataForm{

    public RootNodeForm(Metadata metadata) {
        super(metadata);
    }

    @Override
    protected void createForm() {
        formInstance = Form.of(
            Group.of(
                Field.ofStringType(metadata.nameProperty())
                        .label("Имя")
                        .required("Не указано имя")
                        .validate(RegexValidator.forNames("Не верно задано имя"))
            )
        ).title("Метаданные");
    }

}
