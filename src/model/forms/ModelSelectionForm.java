package model.forms;

import javafx.beans.property.ListProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import model.core.Metadata;
import model.core.factory.MetadataFactory;

import java.util.List;
import java.util.Optional;

public class ModelSelectionForm {

    ListProperty<Metadata> metadataList;
    Metadata currentValue;

    ModelSelectionForm(Metadata currentValue) {
        this.currentValue = currentValue;
        this.metadataList = MetadataFactory.getInstance().getRootNode().propertiesProperty();
    }

    private TableView<Metadata> getTableView() {
        TableView<Metadata> table = new TableView<>(metadataList);

        TableColumn<Metadata,String> firstColumn = new TableColumn<>("Модель");
        firstColumn.setCellValueFactory(p -> p.getValue().nameProperty());
        table.getColumns().add(firstColumn);

        table.getSelectionModel().select(currentValue);

        firstColumn.prefWidthProperty().bind(table.widthProperty().subtract(20));

        return table;
    }

    public Optional<Metadata> open() {
        TableView<Metadata> table = getTableView();

        ButtonType buttonOK = new ButtonType("Выбрать", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        Dialog<Metadata> dialog = new Dialog<>();
        dialog.setTitle("Выбор модели");
        dialog.getDialogPane().setContent(table);
        dialog.getDialogPane().getButtonTypes().addAll(buttonOK, buttonCancel);
        dialog.setResultConverter(p -> {
            if (p.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return table.getSelectionModel().getSelectedItem();
            } else {
                return null;
            }
        });
        table.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                dialog.setResult(table.getSelectionModel().getSelectedItem());
                dialog.close();
            }
        });
        return dialog.showAndWait();
    }
}
