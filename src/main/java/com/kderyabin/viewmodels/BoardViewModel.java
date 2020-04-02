package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.mapping.ModelWrapper;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class BoardViewModel implements ViewModel {

    private NavigateServiceInterface navigation;
    private BoardModel model;
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();

    public void initialize() {
        if(model != null) {
            setName(model.getName());
            setDescription(model.getDescription());
            if(!model.getParticipants().isEmpty()){
                participants.addAll(
                        model.getParticipants()
                                .stream()
                                .map(PersonListItemViewModel::new)
                                .collect(Collectors.toList())
                );
            }
        } else {
            model = new BoardModel();
        }
    }

    public void goBack() {
        try {
            navigation.navigate("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
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

    public ObservableList<PersonListItemViewModel> participantsProperty() {
        return participants;
    }


    /**
     * Add new participant to the list.
     * @param name Participant name.
     * @return  TRUE on success False en failure.
     */
    public boolean addParticipant(String name) {
        PersonModel model = new PersonModel();
        model.setName(name);
        PersonListItemViewModel viewModel = new PersonListItemViewModel(model);
        return participants.add(viewModel);
    }

    public void removeParticipant(PersonListItemViewModel personListItemViewModel){
        participants.remove(personListItemViewModel);
    }

    /**
     * Save board data.
     * @return TRUE if saved with success  FALSE on failure.
     */
    public boolean save(){
        model.setName(getName());
        model.setDescription(getDescription());
        return true;
    }
}
