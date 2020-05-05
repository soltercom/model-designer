package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import model.controller.MetadataController;
import model.controller.MetadataControllerEvent;
import model.core.factory.MetadataFactory;
import model.forms.RootNodeForm;
import model.test.DemoModel;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFx implements Initializable {

    @FXML
    private BorderPane rootPane;

    private MetadataController metadataController;

    private void updatePropertyForm() {
        rootPane.setRight(metadataController.getPropertyForm());
    }

    private void updateNewPropertyForm() {
        rootPane.setRight(metadataController.getNewPropertyForm());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        metadataController = MetadataController.getInstance();
        rootPane.setCenter(metadataController.getTreeView());

        metadataController.addEventHandler(MetadataControllerEvent.SELECTED_METADATA_CHANGED,
                e -> updatePropertyForm());

        metadataController.addEventHandler(MetadataControllerEvent.NEW_METADATA,
                e -> updateNewPropertyForm());
    }
}
