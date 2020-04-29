package com.kderyabin.viewmodels;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.repository.BoardRepository;
import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.repository.PersonRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Component
@Scope("prototype")
//@Transactional
//@ScopeProvider(BoardScope.class)
public class BoardFormViewModel implements ViewModel {

    final private Logger LOG = LoggerFactory.getLogger(BoardFormViewModel.class);
    /*
     * Dependencies
     */
    private NavigateServiceInterface navigation;
    private NotificationCenter notificationCenter;
    private BoardRepository repository;
    private PersonRepository personRepository;
    private BoardModel model;

    private BoardScope scope;
    /*
     * ViewModel properties for binding.
     */
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    /**
     * List of participants attached to the board.
     */
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();
    /**
     * The list of all participants created in the application.
     * Used to populate board's participants list.
     */
    private ObservableList<PersonListItemViewModel> persons = FXCollections.observableArrayList();

    /**
     * Helper field.
     * Updated whenever participants list is updated.
     */
    private boolean personListUpdated = false;

    protected void initModel() {

        if (scope.getBoardModel() != null) {
            setModel(scope.getBoardModel());
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
        persons.addAll(getPersonsList());
    }

    /**
     * Fetch participants from DB and convert them PersonListItemViewModel for display in a view.
     * @return
     */
    private List<PersonListItemViewModel> getPersonsList(){

        return StreamSupport
                .stream(  personRepository.findAll().spliterator(), false)
                .map(PersonListItemViewModel::new)
                .collect(Collectors.toList());
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

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ObservableList<PersonListItemViewModel> getPersons() {
        return persons;
    }

    public void setPersons(ObservableList<PersonListItemViewModel> persons) {
        this.persons = persons;
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
        personListUpdated = true;
        return participants.add(viewModel);
    }
    /**
     * Add new participant to the list.
     * Error from here is dispatch through notification center.
     *
     * @param name Participant name.
     * @return TRUE on success False if participant exists already.
     */
    public boolean addParticipant(PersonListItemViewModel viewModel) {
        // Check if already in the list
        if (participants.contains(viewModel)) {
            notificationCenter.publish(
                    Notification.INFO_DISMISS, "msg.participant_already_on_board"
            );
            return false;
        }

        personListUpdated = true;
        return participants.add(viewModel);
    }
    /**
     * @param personListItemViewModel View model passed from the View.
     * @return TRUE if removed FALSE if not or not found in the list.
     */
    public boolean removeParticipant(PersonListItemViewModel personListItemViewModel) {
        personListUpdated = true;
        return participants.remove(personListItemViewModel);
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
                !personListUpdated;
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
            throw new ValidationException("msg.provide_participants");
        }
    }

    /**
     * Load previous view.
     *
     * @throws ViewNotFoundException See NavigationService.navigate()
     */
    public void goBack() throws ViewNotFoundException {
        System.out.println("Scope has board: " + scope.isHasBoards());
        final String view = scope.isHasBoards() ? "home" : "start";
        navigation.navigate(view);
    }

    /**
     * Save board data and load next view.
     */
    @Transactional
    public void save() throws ViewNotFoundException {
        try {
            validate();

            model.setName(getName());
            model.setDescription(getDescription());
            model.initUpdateTime();
            model.getParticipants().clear();
            participants.forEach(person -> model.addParticipant(person.getModel()));

            model = repository.save(model);
            scope.setBoardModel(model);
            notificationCenter.publish(Notification.INFO, "msg.board_saved_success");
            // Can be null in unit tests.
            if (null != navigation) {
                navigation.navigate("board-item");
            }
        } catch (ValidationException e) {
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        }
    }

    public void setModel(BoardModel model) {
        this.model = model;
    }
}
