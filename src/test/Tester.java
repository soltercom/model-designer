package test;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import test.view.Form;
import test.view.StringControl;


public class Tester {

    private TestModel model;

    public Tester() {
        model = new TestModel("Petrov", "Petr");
    }

    public Pane getContainer() {
        Form form = new Form();
        form.add(StringControl.getStringControl(model.firstNameProperty()).label("Имя"));
        form.add(StringControl.getStringControl(model.secondNameProperty()).label("Фамилия"));
        return form.getContainer();
    }

}
