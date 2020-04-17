package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@ScopeProvider(BoardScope.class)
public class ItemEditViewModel implements ViewModel {
    private static Logger LOG = LoggerFactory.getLogger(ItemEditViewModel.class);

    private NavigateServiceInterface navigation;
    private BoardItemModel model;
    private BoardScope scope;

    // Properties
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<PersonListItemViewModel> person = new SimpleObjectProperty<>();
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();

    public void initialize(){
        LOG.info(">> In initialize() method");
        model = new BoardItemModel();
        model.setBoard(scope.getModel());
        Set<PersonModel> persons = scope.getModel().getParticipants();
        participants.addAll(
                persons.stream()
                        .map(PersonListItemViewModel::new)
                        .collect(Collectors.toList())
        );
    }

    public void goBack() throws Exception {
        navigation.navigate("board-items");
    }


    public boolean canGoBack() {
        return true;
    }
    public void save() {
    }

    @Autowired
    public void setScope(BoardScope scope) {
        this.scope = scope;
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

    public String getAmount() {
        return amount.get();
    }

    public StringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public ObservableList<PersonListItemViewModel> getParticipants() {
        return participants;
    }

    public void setParticipants(ObservableList<PersonListItemViewModel> participants) {
        this.participants = participants;
    }

    public PersonListItemViewModel getPerson() {
        return person.get();
    }

    public ObjectProperty<PersonListItemViewModel> personProperty() {
        return person;
    }

    public void setPerson(PersonListItemViewModel person) {
        this.person.set(person);
    }
}
