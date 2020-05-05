package formsfx.view.renderer;

import formsfx.model.structure.Group;
import formsfx.view.renderer.GroupRendererBase;
import javafx.geometry.Insets;

public class GroupRenderer extends GroupRendererBase {

    protected GroupRenderer(Group group) {
        this.element = group;
        init();
    }

    public void initializeParts() {
        super.initializeParts();
    }

    public void layoutParts() {
        super.layoutParts();

        setPadding(new Insets(SPACING * 2));
        getChildren().add(grid);
    }

}
