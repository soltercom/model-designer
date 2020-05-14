package form.control;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.CheckBox;

public class CheckBoxController extends ViewController {

    public CheckBoxController(PropertyController<?> propertyController) {
        super(propertyController);
        this.view = new CheckBox();
        setBindings();
        setStyles();
    }

    protected void setBindings() {
        super.setBindings();
        ((CheckBox) view).selectedProperty().bindBidirectional((BooleanProperty)propertyController.getProperty());
    }

    protected  void setStyles() {
        view.getStyleClass().add("check-box-view");
        updateStyle(CHANGED_CLASS, propertyController.isChanged());
    }

}
