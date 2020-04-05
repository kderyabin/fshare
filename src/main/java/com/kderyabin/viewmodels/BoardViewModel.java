package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
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
    private BoardRepository repository;


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

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    @Autowired
    public void setRepository(BoardRepository repository) {
        this.repository = repository;
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
        PersonModel personModel = new PersonModel();
        personModel.setName(name);
        model.addParticipant(personModel);

        PersonListItemViewModel viewModel = new PersonListItemViewModel(personModel);
        return participants.add(viewModel);
    }

    public void removeParticipant(PersonListItemViewModel personListItemViewModel){
        PersonModel personModel = personListItemViewModel.getModel();
        model.removeParticipant(personModel);
        participants.remove(personListItemViewModel);
    }

    /**
     * Validate data integrity.
     *
     * @throws Exception
     */
    public void validate() throws Exception{
        if(getName().isEmpty()) {
            throw new Exception("Board name is required!");
        }

        if(participants.size() == 0) {
            throw new Exception("Add participants!");
        }

    }


    public void goBack() {
        try {
            navigation.navigate("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Save board data.
     */
    public void save() throws Exception{
        validate();

        model.setName(getName());
        model.setDescription(getDescription());
        model.initUpdateTime();

        model = repository.save(model);
        navigation.navigate("start");
    }
}
