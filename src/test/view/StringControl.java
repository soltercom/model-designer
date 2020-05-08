package test.view;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;


public class StringControl extends DataControl<StringProperty> {

    private TextField textFieldControl;

    public StringControl(StringProperty value) {
        super(value);
        this.value = new SimpleStringProperty(value.get());
        init();
    }

    @Override
    public void initNodes() {
        super.initNodes();
        textFieldControl = new TextField();
        labelControl.setLabelFor(textFieldControl);
    }

    @Override
    public void initBindings() {
        super.initBindings();
        textFieldControl.textProperty().bindBidirectional(value);
        changedProperty().bind(Bindings.notEqual(value, persistValue));
    }

    public Pane getContainer() {
        int COLUMN_COUNT = 12;
        int SPAN = 3;

        GridPane container = new GridPane();

        for (int i = 0; i < COLUMN_COUNT; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0/COLUMN_COUNT);
            container.getColumnConstraints().add(column);
        }

        container.setAlignment(Pos.CENTER);

        container.add(labelControl, 0, 0, SPAN, 1);
        container.add(textFieldControl, SPAN, 0, COLUMN_COUNT - SPAN, 1);

        GridPane.setMargin(labelControl, new Insets(SPACING, SPACING, 0, 0));
        GridPane.setMargin(textFieldControl, new Insets(SPACING, SPACING, 0, 0));
        GridPane.setHalignment(labelControl, HPos.RIGHT);
        GridPane.setHalignment(textFieldControl, HPos.LEFT);

        return container;
    }

}
