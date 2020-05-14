package form.control;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class ViewController {

    protected static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");
    protected static final PseudoClass CHANGED_CLASS = PseudoClass.getPseudoClass("changed");

    protected Control view;
    protected PropertyController<?> propertyController;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("label", new Locale("ru"));

    public ViewController(PropertyController<?> propertyController) {
        this.propertyController = propertyController;
    }

    public Node getView() {
        return view;
    }

    public Label getLabel() {
        String title = resourceBundle.getString(propertyController.getField().getName()+"-label");
        Label label = new Label(title);
        label.labelForProperty().setValue(this.getView());
        return label;
    }

    public PropertyController<?> getPropertyController() {
        return propertyController;
    }

    protected void setBindings() {
        propertyController.changedProperty().addListener((observable, oldValue, newValue) -> updateStyle(CHANGED_CLASS, newValue));
        propertyController.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(INVALID_CLASS, !newValue));
    }

    protected void updateStyle(PseudoClass pseudo, boolean newValue) {
        view.pseudoClassStateChanged(pseudo, newValue);
    }

    abstract void setStyles();

}
