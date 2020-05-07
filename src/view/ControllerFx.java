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
        rootPane.setCenter(metadataController.getPropertyForm());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        metadataController = MetadataController.getInstance();
        rootPane.setLeft(metadataController.getTreeView());

        metadataController.addEventHandler(MetadataControllerEvent.SELECTED_METADATA_CHANGED,
                e -> updatePropertyForm());

    }
}
