package model.test;

import formsfx.model.structure.Field;
import formsfx.model.structure.Form;
import formsfx.model.structure.Group;
import formsfx.view.renderer.FormRenderer;
import formsfx.view.util.ViewMixin;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class DemoModel {

    private class TestModel {
        private StringProperty first = new SimpleStringProperty("");
        private StringProperty second = new SimpleStringProperty("");
        private StringProperty third = new SimpleStringProperty("");

        public String getFirst() {
            return first.get();
        }
        public StringProperty firstProperty() {
            return first;
        }
        public String getSecond() {
            return second.get();
        }
        public StringProperty secondProperty() {
            return second;
        }
        public String getThird() {
            return third.get();
        }
        public StringProperty thirdProperty() {
            return second;
        }
    }

    private static class TestModelPane extends BorderPane implements ViewMixin {

        private DemoModel model;

        private FormRenderer displayForm;

        private ScrollPane scrollContent;
        private Button save;
        private Button reset;
        private HBox formButtons;

        public TestModelPane(DemoModel model) {
            this.model = model;
            init();
        }

        public void initializeSelf() {}

        public void initializeParts() {
            save = new Button("Save");
            reset = new Button("Reset");
            formButtons = new HBox();

            scrollContent = new ScrollPane();

            displayForm = new FormRenderer(model.getFormInstance());
        }

        public void setupBindings() {
            save.disableProperty().bind(model.getFormInstance().persistableProperty().not());
            reset.disableProperty().bind(model.getFormInstance().changedProperty().not());
        }

        public void setupValueChangedListeners() {
            model.getFormInstance().changedProperty().addListener((observable, oldValue, newValue) -> System.out.println("The form has " + (newValue ? "" : "not ") + "changed."));
            model.getFormInstance().persistableProperty().addListener((observable, oldValue, newValue) -> System.out.println("The form is " + (newValue ? "" : "not ") + "persistable."));

            model.getTestModel().firstProperty().addListener((observable, oldValue, newValue) -> System.out.println("First: " + newValue));
            model.getTestModel().secondProperty().addListener((observable, oldValue, newValue) -> System.out.println("Second: " + newValue));
            model.getTestModel().thirdProperty().addListener((observable, oldValue, newValue) -> System.out.println("Third: " + newValue));
        }

        public void setupEventHandlers() {
            reset.setOnAction(event -> model.getFormInstance().reset());
            save.setOnAction(event -> model.getFormInstance().persist());
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

    private TestModel testModel = new TestModel();
    TestModel getTestModel() {
        return testModel;
    }

    private Form formInstance;
    public Form getFormInstance() {
        if (formInstance == null) {
            createForm();
        }
        return formInstance;
    }

    private void createForm() {
        formInstance = Form.of(
            Group.of(
                Field.ofStringType(testModel.firstProperty()).label("Фамилия"),
                Field.ofStringType(testModel.secondProperty()).label("Имя"),
                Field.ofStringType(testModel.thirdProperty()).label("Отчество")
            )
        ).title("Тестовая форма");
    }

    static public BorderPane getPane() {
        DemoModel demoModel = new DemoModel();
        return new TestModelPane(demoModel);
    }

}
