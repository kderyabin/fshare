package com.kderyabin.viewmodels;

import com.kderyabin.model.BoardItemModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;

public class LinesListItemViewModel implements ViewModel {

    private BoardItemModel model;

    private StringProperty title = new SimpleStringProperty("");
    private StringProperty person = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private StringProperty currency = new SimpleStringProperty("");
    private StringProperty date = new SimpleStringProperty("");

    public LinesListItemViewModel() {
    }

    public LinesListItemViewModel(BoardItemModel model) {
        this.model = model;
        setTitle(model.getTitle());
        setPerson(model.getPerson().getName());
        setAmount(model.getAmount().toString());
        setCurrency(model.getBoard().getCurrencyCode());
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
        setDate(df.format(model.getDate()));
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

    public String getCurrency() {
        return currency.get();
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
