package test.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Form implements ViewInit {

    private Button buttonOK;
    private Button buttonCancel;
    private final List<SimpleControl> controlList;

    protected final BooleanProperty changed = new SimpleBooleanProperty(false);

    public Form() {
        controlList = new ArrayList<>();
        init();
    }

    @Override
    public void initNodes() {
        buttonOK = new Button("Записать");
        buttonOK.setOnAction(event -> controlList.forEach(SimpleControl::save));
        buttonCancel = new Button("Отменить");
        buttonCancel.setOnAction(event -> controlList.forEach(SimpleControl::cancel));
    }


    public void initBindings() {
        buttonOK.disableProperty().bind(changedProperty().not());
        buttonCancel.disableProperty().bind(changedProperty().not());
    }

    public SimpleControl add(SimpleControl control) {
        controlList.add(control);
        control.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty());
        return control;
    }

    public Pane getContainer() {
        BorderPane container = new BorderPane();

        VBox vbox = new VBox();
        for (SimpleControl control: controlList) {
            vbox.getChildren().add(control.getContainer());
        }
        container.setCenter(vbox);

        ButtonBar buttonBar = new ButtonBar();
        ButtonBar.setButtonData(buttonOK, ButtonBar.ButtonData.OK_DONE);
        ButtonBar.setButtonData(buttonCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
        buttonBar.getButtons().addAll(buttonOK, buttonCancel);
        HBox hBox = new HBox(buttonBar);
        hBox.setAlignment(Pos.CENTER);
        HBox.setMargin(buttonBar, new Insets(10.0));
        container.setBottom(hBox);

        return container;
    }

    protected void setChangedProperty() {
        changed.setValue(controlList.stream().anyMatch(SimpleControl::hasChanged));
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }
}
