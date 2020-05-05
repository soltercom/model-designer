package formsfx.view.controls;

import formsfx.model.structure.Field;
import formsfx.view.util.ViewMixin;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public abstract class SimpleControl <F extends Field> extends GridPane implements ViewMixin {

    protected F field;

    public void setField(F field) {
        if (this.field != null) {
            throw new IllegalStateException("Cannot change a control's field once set.");
        }

        this.field = field;
        init();
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

}
