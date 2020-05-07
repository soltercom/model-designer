package formsfx.view.controls;

import formsfx.model.structure.Field;
import formsfx.view.util.ViewMixin;
import javafx.collections.ListChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public abstract class SimpleControl <F extends Field> extends GridPane implements ViewMixin {

    protected F field;

    protected static final PseudoClass REQUIRED_CLASS = PseudoClass.getPseudoClass("required");
    protected static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");
    protected static final PseudoClass CHANGED_CLASS = PseudoClass.getPseudoClass("changed");

    public void setField(F field) {
        if (this.field != null) {
            throw new IllegalStateException("Cannot change a control's field once set.");
        }

        this.field = field;
        init();
    }

    public void initializeParts() {
        getStyleClass().add("simple-control");

        getStyleClass().addAll(field.getStyleClass());

        updateStyle(INVALID_CLASS, !field.isValid());
        updateStyle(REQUIRED_CLASS, field.isRequired());
        updateStyle(CHANGED_CLASS, field.hasChanged());
    }

    public void layoutParts() {
        setAlignment(Pos.CENTER_LEFT);

        int columns = field.getSpan();

        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / columns);
            getColumnConstraints().add(colConst);
        }

    }

    public void setupBindings() {
        idProperty().bind(field.idProperty());
    }

    @Override
    public void setupValueChangedListeners() {
        field.validProperty().addListener((observable, oldValue, newValue) -> updateStyle(INVALID_CLASS, !newValue));
        field.requiredProperty().addListener((observable, oldValue, newValue) -> updateStyle(REQUIRED_CLASS, newValue));
        field.changedProperty().addListener((observable, oldValue, newValue) -> updateStyle(CHANGED_CLASS, newValue));

        field.getStyleClass().addListener((ListChangeListener<String>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    getStyleClass().removeAll(c.getRemoved());
                }

                if (c.wasAdded()) {
                    getStyleClass().addAll(c.getAddedSubList());
                }
            }
        });
    }

    protected void updateStyle(PseudoClass pseudo, boolean newValue) {
        pseudoClassStateChanged(pseudo, newValue);
    }

    public void addStyleClass(String name) {
        getStyleClass().add(name);
    }

    public void removeStyleClass(String name) {
        getStyleClass().remove(name);
    }

}
