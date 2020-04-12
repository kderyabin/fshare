package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Component
@Scope("prototype")
public class BoardViewModel implements ViewModel {

    /*
     * Dependencies
     */
    private NavigateServiceInterface navigation;
    private NotificationCenter notificationCenter;
    private BoardRepository repository;
    private BoardModel model;
    private BoardScope scope;
    /*
     * ViewModel properties for binding.
     */
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();

    private List<PersonModel> addedPersons = new ArrayList<>();
    private List<PersonModel> removedPersons = new ArrayList<>();

    @Autowired
    EntityManager entityManager;

    protected void initModel() {
        if (scope != null && scope.getModel() != null) {
            Optional<BoardModel> found = repository.findById(scope.getModel().getId());
            if (found.isPresent()) {
                setModel(found.get());
            }
        }
    }

    public void initialize() {
        initModel();
        if (model != null) {
            setName(model.getName());
            setDescription(model.getDescription());
            if (!model.getParticipants().isEmpty()) {
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

    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public BoardScope getScope() {
        return scope;
    }

    @Autowired
    public void setScope(BoardScope scope) {
        this.scope = scope;
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

    public ObservableList<PersonListItemViewModel> getParticipants() {
        return participants;
    }

    /**
     * Add new participant to the list.
     * Error from here is dispatch through notification center.
     *
     * @param name Participant name.
     * @return TRUE on success False if participant exists already.
     */
    public boolean addParticipant(String name) {
        if (name == null || name.trim().isEmpty()) {
            notificationCenter.publish(
                    Notification.INFO_DISMISS, "msg.partipant_name_required"
            );
            return false;
        }
        PersonModel personModel = new PersonModel();
        personModel.setName(name.trim());

        PersonListItemViewModel viewModel = new PersonListItemViewModel(personModel);
        addedPersons.add(personModel);
        return participants.add(viewModel);
    }

    /**
     * @param personListItemViewModel View model passed from the View.
     * @return TRUE if removed FALSE if not or not found in the list.
     */
    public boolean removeParticipant(PersonListItemViewModel personListItemViewModel) {
        removedPersons.add(personListItemViewModel.getModel());
        return participants.remove(personListItemViewModel);
    }

    private boolean isPersonsListUpdated() {
        return !addedPersons.isEmpty() || !removedPersons.isEmpty();
    }

    /**
     * Checks if model data has been updated.
     *
     * @return TRUE if we can safely quit the scene FALSE if there are modifications to be saved.
     */
    public boolean canGoBack() {
        // Is it a new board?
        if (model.getId() == null) {
            return getName().trim().equals("") &&
                    getDescription().trim().equals("") &&
                    participants.size() == 0;
        }
        // For existing model check if data match
        return getName().equals(model.getName()) &&
                getDescription().equals(model.getDescription()) &&
                !isPersonsListUpdated();
    }

    /**
     * Validate data integrity.
     *
     * @throws ValidationException Validation exception with resource id as a message.
     *                             The human message will be retrieved during GUI display.
     */
    public void validate() throws ValidationException {
        if (getName().trim().isEmpty()) {
            throw new ValidationException("msg.board_name_required");
        }

        if (participants.size() == 0) {
            throw new ValidationException("msg.provide_participant");
        }
    }

    /**
     * Load previous view.
     *
     * @throws Exception See NavigationService.navigate()
     */
    public void goBack() throws Exception {
        navigation.navigate("home");
//        if(navigation.hasPrevious()){
//            navigation.goToPrevious();
//        }
    }

    /**
     * Save board data and load next view.
     */
    public void save() throws Exception {
        try {
            validate();

            model.setName(getName());
            model.setDescription(getDescription());
            model.initUpdateTime();
            if (!removedPersons.isEmpty()) {
                model.getParticipants().removeAll(removedPersons);
                removedPersons.clear();
            }
            if (!addedPersons.isEmpty()) {
                model.getParticipants().addAll(addedPersons);
                addedPersons.clear();
            }

            model = repository.save(model);
            notificationCenter.publish(Notification.INFO, "msg.board_saved_success");
            // Can be null in unit tests.
            if (null != navigation) {
                navigation.navigate("home");
            }
        } catch (ValidationException e) {
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        }
    }

    public void setModel(BoardModel model) {
        this.model = model;
    }
}
