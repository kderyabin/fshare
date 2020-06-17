package com.kderyabin.mvvm.participant.list;

import com.kderyabin.model.PersonModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;


public class PersonListItemViewModel implements ViewModel {
    private StringProperty name = new SimpleStringProperty();
    private PersonModel model;

    public PersonListItemViewModel() {
    }

    public PersonListItemViewModel(PersonModel model) {
        this.model = model;
        setName(this.model.getName());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonListItemViewModel)) return false;
        PersonListItemViewModel that = (PersonListItemViewModel) o;
        return model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }
}
