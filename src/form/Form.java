package form;

import form.control.*;
import form.utils.ReflectionUtils;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import model.controller.MetadataControllerEvent;
import model.core.Metadata;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Form<T extends Metadata> extends VBox{

    private final T source;
    private final List<ViewController> viewControllerList;

    private final BooleanProperty changed = new SimpleBooleanProperty(false);
    private final BooleanProperty valid = new SimpleBooleanProperty(true);

    public static Pane empty() {
        return new VBox();
    }

    public Form(T source) {
        this.source = source;
        viewControllerList = createViewControllerList();
        initView();
        setBindings();
    }

    private void formChangedListener() {
        boolean value = viewControllerList.stream()
            .map(ViewController::getPropertyController)
            .anyMatch(PropertyController::isChanged);
        changed.setValue(value);
    }

    private void formValidListener() {
        boolean value = viewControllerList.stream()
                .map(ViewController::getPropertyController)
                .allMatch(PropertyController::isValid);
        valid.setValue(value);
    }

    private void setBindings() {
        viewControllerList.stream()
            .map(ViewController::getPropertyController)
            .map(PropertyController::changedProperty)
            .forEach(changedProperty -> changedProperty.addListener(v -> formChangedListener()));
        viewControllerList.stream()
                .map(ViewController::getPropertyController)
                .map(PropertyController::validProperty)
                .forEach(validProperty -> validProperty.addListener(v -> formValidListener()));
    }

    private List<ViewController> createViewControllerList() {
        List<ViewController> list = new ArrayList<>();
        for (Field field: ReflectionUtils.listFields(source.getClass())) {
            try {
                if (field.getType().equals(StringProperty.class)) {
                    String value = ((StringProperty)field.get(source)).getValue();
                    StringProperty property = new SimpleStringProperty(value);
                    StringProperty persistProperty = new SimpleStringProperty(value);
                    list.add(new StringTextFieldController(new PropertyController<>(field, property, persistProperty)));
                }
                if (field.getType().equals(BooleanProperty.class)) {
                    boolean value = ((BooleanProperty)field.get(source)).getValue();
                    BooleanProperty property = new SimpleBooleanProperty(value);
                    BooleanProperty persistProperty = new SimpleBooleanProperty(value);
                    list.add(new CheckBoxController(new PropertyController<>(field, property, persistProperty)));
                }
                if (field.getType().equals(ObjectProperty.class)) {
                    Object value = ((ObjectProperty)field.get(source)).getValue();
                    ObjectProperty property = new SimpleObjectProperty(value);
                    ObjectProperty persistProperty = new SimpleObjectProperty(value);
                    list.add(new ModelTextFieldController(new PropertyController<Boolean>(field, property, persistProperty)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void save() {
        viewControllerList.stream() // save properties
            .map(ViewController::getPropertyController)
            .forEach(PropertyController::save);
        viewControllerList.stream() // save source
                .map(ViewController::getPropertyController)
                .forEach(property -> {
                    try {
                        property.getField().set(source, property.getProperty());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        fireEvent(FormEvent.formEventPersisted());
    }

    private void cancel() {
        viewControllerList.stream()
                .map(ViewController::getPropertyController)
                .forEach(PropertyController::cancel);
    }

    private void initView() {
        int COLUMN_COUNT = 12;
        int SPAN = 3;
        double SPACING = 10.0;

        GridPane container = new GridPane();
        for (int i = 0; i < COLUMN_COUNT; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0/COLUMN_COUNT);
            container.getColumnConstraints().add(column);
        }
        container.setAlignment(Pos.CENTER);

        for (int i = 0; i < viewControllerList.size(); i++) {
            ViewController controller = viewControllerList.get(i);
            Label labelNode = controller.getLabel();
            Node viewNode = controller.getView();

            container.add(labelNode, 0, i, SPAN, 1);
            container.add(viewNode, SPAN, i, COLUMN_COUNT - SPAN, 1);

            GridPane.setMargin(labelNode, new Insets(SPACING, SPACING, 0, 0));
            GridPane.setMargin(viewNode, new Insets(SPACING, SPACING, 0, 0));
            GridPane.setHalignment(labelNode, HPos.RIGHT);
            GridPane.setHalignment(viewNode, HPos.LEFT);
        }

        HBox buttons = new HBox(SPACING);
        Button okButton = new Button("Записать");
        okButton.setOnAction(e -> save());
        okButton.disableProperty().bind(changed.not().or(valid.not()));
        Button cancelButton = new Button("Оменить");
        cancelButton.setOnAction(e -> cancel());
        cancelButton.disableProperty().bind(changed.not());
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(okButton, cancelButton);

        getChildren().addAll(container, buttons);

        setSpacing(SPACING);
        setMargin(buttons, new Insets(SPACING));

    }

    @Override
    public String getUserAgentStylesheet() {
        return Form.class.getResource("style.css").toExternalForm();
    }

}
