package model.forms;

import formsfx.view.renderer.FormRenderer;
import formsfx.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MetadataPane extends BorderPane implements ViewMixin {

    private MetadataForm metadataForm;

    private FormRenderer displayForm;

    private ScrollPane scrollContent;
    private Button save;
    private Button reset;
    private HBox formButtons;

    public MetadataPane(MetadataForm metadataForm) {
        this.metadataForm = metadataForm;
        init();
    }

    public void initializeSelf() {}

    public void initializeParts() {
        save = new Button("Записать");
        reset = new Button("Отмена");
        formButtons = new HBox();

        scrollContent = new ScrollPane();

        displayForm = new FormRenderer(metadataForm.getFormInstance());
    }

    public void setupBindings() {
        save.disableProperty().bind(metadataForm.getFormInstance().persistableProperty().not());
        reset.disableProperty().bind(metadataForm.getFormInstance().changedProperty().not());
    }

    public void setupValueChangedListeners() {
        //metadataFrom.getFormInstance().changedProperty().addListener((observable, oldValue, newValue) -> System.out.println("The form has " + (newValue ? "" : "not ") + "changed."));
        //metadataFrom.getFormInstance().persistableProperty().addListener((observable, oldValue, newValue) -> System.out.println("The form is " + (newValue ? "" : "not ") + "persistable."));

        //metadataFrom.getMetadata().nameProperty().addListener((observable, oldValue, newValue) -> System.out.println(metadataFrom.getMetadata().printMetadata(0)));
    }

    public void setupEventHandlers() {
        reset.setOnAction(event -> metadataForm.getFormInstance().reset());
        save.setOnAction(event -> metadataForm.getFormInstance().persist());
    }

    public void layoutParts() {
        save.setMaxWidth(Double.MAX_VALUE);
        reset.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(save, Priority.ALWAYS);
        HBox.setHgrow(reset, Priority.ALWAYS);
        formButtons.getChildren().addAll(reset, save);
        formButtons.setPadding(new Insets(10));
        formButtons.setSpacing(10);
        formButtons.setPrefWidth(200);

        scrollContent.setContent(displayForm);
        scrollContent.setFitToWidth(true);

        setCenter(scrollContent);
        setBottom(formButtons);
    }

}
