package formsfx.view.renderer;

import formsfx.model.structure.Element;
import formsfx.model.structure.Field;
import formsfx.model.structure.Group;
import formsfx.model.structure.NodeElement;
import formsfx.view.controls.SimpleControl;
import formsfx.view.util.ViewMixin;
import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class GroupRendererBase<V extends Group> extends StackPane implements ViewMixin {

    protected final int SPACING = 10;

    protected GridPane grid;

    protected V element;

    public void initializeParts() {
        grid = new GridPane();
    }

    public void layoutParts() {
        int COLUMN_COUNT = 12;

        for (int i = 0; i < COLUMN_COUNT; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / COLUMN_COUNT);
            grid.getColumnConstraints().add(colConst);
        }

        grid.setHgap(SPACING);
        grid.setVgap(SPACING);
        setPadding(new Insets(SPACING));

        int currentRow = 0;
        int currentColumnCount = 0;

        for (Element e : element.getElements()) {
            int span = e.getSpan();

            if (currentColumnCount + span > COLUMN_COUNT) {
                currentRow += 1;
                currentColumnCount = 0;
            }

            if (e instanceof Field) {
                Field f = (Field) e;
                SimpleControl c = f.getRenderer();
                c.setField(f);

                grid.add(c, currentColumnCount, currentRow, span, 1);
            } else if (e instanceof NodeElement){
                grid.add(((NodeElement)e).getNode(), currentColumnCount, currentRow, span, 1);
            }

            currentColumnCount += span;
        }

    }
}
