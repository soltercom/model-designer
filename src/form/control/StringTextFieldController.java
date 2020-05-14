package form.control;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class StringTextFieldController extends ViewController {

    public StringTextFieldController(PropertyController<?> propertyController) {
        super(propertyController);
        this.view = new TextField();
        setBindings();
        setStyles();
    }

    protected void setBindings() {
        super.setBindings();
        ((TextField)view).textProperty().bindBidirectional((StringProperty)propertyController.getProperty());
        ((TextField)view).textProperty().addListener((observable, oldValue, newValue) -> propertyController.validate());
    }

    protected void setStyles() {
        view.getStyleClass().add("string-text-field-view");
        updateStyle(CHANGED_CLASS, propertyController.isChanged());
        updateStyle(INVALID_CLASS, !propertyController.isValid());
    }


}
