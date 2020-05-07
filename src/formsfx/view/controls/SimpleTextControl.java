package formsfx.view.controls;

import formsfx.model.structure.StringField;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class SimpleTextControl extends SimpleControl<StringField> {

    protected StackPane stack;
    protected TextField editableField;
    protected Label fieldLabel;

    public void initializeParts() {
        super.initializeParts();

        stack = new StackPane();
        editableField = new TextField(field.getValue());
        fieldLabel = new Label(field.labelProperty().getValue());
    }

    public void layoutParts() {
        super.layoutParts();

        stack.getChildren().addAll(editableField);
        stack.setAlignment(Pos.CENTER_LEFT);

        int columns = field.getSpan();
        if (columns < 3) {
            int rowIndex = 0;
            add(fieldLabel, 0, rowIndex++, columns, 1);
            add(stack, 0, rowIndex++, columns, 1);
        } else {
            add(fieldLabel, 0, 0, 2, 1);
            add(stack, 2, 0, columns - 2, 1);
        }

    }

    public void setupBindings() {
        super.setupBindings();

        editableField.textProperty().bindBidirectional(field.userInputProperty());
        fieldLabel.textProperty().bind(field.labelProperty());
    }

    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();
    }

}
