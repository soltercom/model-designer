package form.control;

import form.ModelSelectionForm;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import model.core.Metadata;

import java.util.Optional;

public class ModelTextFieldController extends ViewController {

    private final Button selectButton;

    public ModelTextFieldController(PropertyController<?> propertyController) {
        super(propertyController);
        view = new TextField();
        selectButton = new Button("...");
        setParameters();
        setBindings();
        setStyles();
    }

    private void setParameters() {
        ((TextField)view).setEditable(false);
        selectButton.setOnAction(e -> onSelectButton());
    }

    protected void setBindings() {
        super.setBindings();
        ((TextField)view).textProperty()
                .bind(Bindings.createStringBinding(() -> propertyController.getProperty().getValue().toString(), propertyController.getProperty()));
    }

    private void onSelectButton() {
        ModelSelectionForm selectionForm = new ModelSelectionForm((Metadata) propertyController.getProperty().getValue());
        Optional<Metadata> result = selectionForm.open();
        result.ifPresent(referenceModel -> ((Property<Metadata>) propertyController.getProperty()).setValue(referenceModel));
    }

    @Override
    public Node getView() {
        StackPane stackPane = new StackPane(view, selectButton);
        StackPane.setAlignment(selectButton, Pos.CENTER_RIGHT);
        return stackPane;
    }

    protected void setStyles() {
        view.getStyleClass().add("model-text-field-view");
        updateStyle(CHANGED_CLASS, propertyController.isChanged());
        updateStyle(INVALID_CLASS, !propertyController.isValid());
    }

}
