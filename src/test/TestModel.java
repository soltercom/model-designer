package test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TestModel {

    private StringProperty firstName;
    private StringProperty secondName;

    public TestModel(String firstName, String secondName) {
        this.firstName = new SimpleStringProperty(firstName);
        this.secondName = new SimpleStringProperty(secondName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty secondNameProperty() {
        return secondName;
    }
}
