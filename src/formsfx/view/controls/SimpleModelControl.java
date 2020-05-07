package formsfx.view.controls;

import formsfx.model.event.FieldEvent;
import formsfx.model.structure.ModelField;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class SimpleModelControl extends SimpleControl<ModelField> {

    protected StackPane stack;
    protected TextField editableField;
    protected Button selectionButton;
    protected Label fieldLabel;

    public void initializeParts() {
        stack = new StackPane();
        editableField = new TextField(field.getValue().getName());
        editableField.setEditable(false);
        selectionButton = new Button("...");
        fieldLabel = new Label(field.labelProperty().getValue());
    }

    public void layoutParts() {
        super.layoutParts();

        stack.getChildren().addAll(editableField, selectionButton);
        stack.setAlignment(Pos.CENTER_RIGHT);

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

        selectionButton.setOnAction(event -> field.fireStartSelectionEvent());
    }

    public void setupValueChangedListeners() {
        super.setupValueChangedListeners();
    }

}
