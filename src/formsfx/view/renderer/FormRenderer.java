package formsfx.view.renderer;

import formsfx.model.structure.Form;
import formsfx.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormRenderer extends VBox implements ViewMixin {

    protected Form form;
    protected List<GroupRendererBase> sections = new ArrayList<>();

    public FormRenderer(Form form) {
        this.form = form;
        init();
    }

    @Override
    public String getUserAgentStylesheet() {
        return FormRenderer.class.getResource("style.css").toExternalForm();
    }

    public void initializeParts() {
        sections = form.getGroups().stream()
                .map(s -> new GroupRenderer(s))
                .collect(Collectors.toList());
    }

    public void layoutParts() {
        setPadding(new Insets(10));
        getChildren().addAll(sections);
    }

}
