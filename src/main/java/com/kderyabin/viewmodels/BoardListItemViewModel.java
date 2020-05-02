package com.kderyabin.viewmodels;

import com.kderyabin.model.BoardModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BoardListItemViewModel implements ViewModel {
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private StringProperty dateUpdate = new SimpleStringProperty("");
    private BoardModel model;

    public BoardListItemViewModel() {
    }

    public BoardListItemViewModel(BoardModel model) {
        this.model = model;
        setName(model.getName());
        setDescription(model.getDescription());
        setDateUpdate(model.getUpdate().toLocalDateTime().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
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

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public BoardModel getModel() {
        return model;
    }

    public void setModel(BoardModel model) {
        this.model = model;
    }

    public String getDateUpdate() {
        return dateUpdate.get();
    }

    public StringProperty dateUpdateProperty() {
        return dateUpdate;
    }

    public void setDateUpdate(String dateUpdate) {
        this.dateUpdate.set(dateUpdate);
    }
}
