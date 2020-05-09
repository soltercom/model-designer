package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import model.controller.MetadataController;
import model.controller.MetadataControllerEvent;
import model.xml.XMLSerialization;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFx implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML private MenuItem menuOpen;
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuNew;

    private MetadataController metadataController;

    private void updatePropertyForm() {
        rootPane.setCenter(metadataController.getPropertyForm());
    }

    private void setMenuItems() {
        menuSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Сохранить файл метаданных");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );
            File savedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
            if (savedFile != null) {
                if (XMLSerialization.getInstance().serialize(savedFile)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, savedFile.getAbsolutePath(), ButtonType.OK);
                    alert.setTitle("Результат операции");
                    alert.setHeaderText("Метаданные сохранены");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                    alert.setTitle("Результат операции");
                    alert.setHeaderText("Ошибка при сохранени файла");
                    alert.show();
                }
            }
        });
        menuOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Открыть файл метаданных");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );
            File openedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (openedFile != null) {
                if (XMLSerialization.getInstance().deserialize(openedFile)) {
                    rootPane.setLeft(metadataController.openRootNode());
                }
            }
        });
        menuNew.setOnAction(e -> rootPane.setLeft(metadataController.newRootNode()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        metadataController = MetadataController.getInstance();
        rootPane.setLeft(metadataController.newRootNode());

        metadataController.addEventHandler(MetadataControllerEvent.SELECTED_METADATA_CHANGED,
                e -> updatePropertyForm());

        setMenuItems();
    }
}
