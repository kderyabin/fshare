package com.kderyabin.viewmodels;

import com.kderyabin.models.PersonModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class PersonListItemViewModel implements ViewModel {
    private StringProperty name = new SimpleStringProperty();
    private PersonModel model;


    public PersonListItemViewModel(PersonModel model) {
        this.model = model;
        name.set(this.model.getName());
    }

    public PersonModel getModel() {
        return model;
    }

    public void setModel(PersonModel model) {
        this.model = model;
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
