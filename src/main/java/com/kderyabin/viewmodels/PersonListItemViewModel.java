package com.kderyabin.viewmodels;

import com.kderyabin.models.PersonModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.stereotype.Component;


public class PersonListItemViewModel implements ViewModel {
//    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private PersonModel model;


    public PersonListItemViewModel(PersonModel model) {
        this.model = model;
//        id.set(this.model.getId());
        name.set(this.model.getName());
    }

    public PersonModel getModel() {
        return model;
    }

    public void setModel(PersonModel model) {
        this.model = model;
    }

//    public long getId() {
//        return id.get();
//    }
//
//    public LongProperty idProperty() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id.set(id);
//    }

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
