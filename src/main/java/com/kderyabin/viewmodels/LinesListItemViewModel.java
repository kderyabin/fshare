package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardItemModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LinesListItemViewModel implements ViewModel {

    private BoardItemModel model;

    private StringProperty title = new SimpleStringProperty("");
    private StringProperty person = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");


    public LinesListItemViewModel() {
    }

    public LinesListItemViewModel(BoardItemModel model) {
        this.model = model;
        setTitle(model.getTitle());
        setPerson(model.getPerson().getName());
        setAmount(model.getAmount().toString());
    }

    public BoardItemModel getModel() {
        return model;
    }

    public void setModel(BoardItemModel model) {
        this.model = model;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getPerson() {
        return person.get();
    }

    public StringProperty personProperty() {
        return person;
    }

    public void setPerson(String person) {
        this.person.set(person);
    }

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }
}
