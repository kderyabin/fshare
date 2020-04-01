package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.Initialize;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BoardViewModel implements ViewModel {

    private NavigateServiceInterface navigation;
    private BoardModel model;
    private ModelWrapper<BoardModel> wrapper = new ModelWrapper<>();
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();
    private ListProperty<PersonListItemViewModel> attendee = new SimpleListProperty<>();

    @Initialize
    public void initialize() {
        model = new BoardModel();
        model.setName("Board title");
        model.setDescription("Board description");
        PersonModel p1 = new PersonModel();
        p1.setName("Sam");
        model.getParticipants().add(p1);
        PersonModel p2 = new PersonModel();
        p2.setName("Jack");
        model.getParticipants().add(p2);
        participants.addAll(model.getParticipants().stream().map(PersonListItemViewModel::new).collect(Collectors.toList()));
        attendee.set(participants);
        wrapper.set(model);
        wrapper.reload();
    }

    public void goBack() {
        try {
            navigation.navigate("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public StringProperty nameProperty() {
        return wrapper.field("name", BoardModel::getName, BoardModel::setName, "");
    }

    public StringProperty descriptionProperty() {
        return wrapper.field("description", BoardModel::getDescription, BoardModel::setDescription, "");
    }

    public ObservableList<PersonListItemViewModel> participantsProperty() {
        return participants;
    }


    public ObservableList<PersonListItemViewModel> getAttendee() {
        return attendee.get();
    }

    public ListProperty<PersonListItemViewModel> attendeeProperty() {
        return attendee;
    }

    public void setAttendee(ObservableList<PersonListItemViewModel> attendee) {
        this.attendee.set(attendee);
    }
}
