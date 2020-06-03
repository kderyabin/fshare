package com.kderyabin.viewmodels;

import com.kderyabin.error.ValidationException;
import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.*;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Component
@Scope("prototype")
public class BoardFormViewModel implements ViewModel, EditableInterface {

    final private Logger LOG = LoggerFactory.getLogger(BoardFormViewModel.class);
    /*
     * Dependencies
     */
    private NavigateServiceInterface navigation;
    private NotificationCenter notificationCenter;
    private StorageManager storageManager;
    private BoardModel model;
    private BoardScope scope;
    private SettingsService settingsService;
    private RunService runService;



    /*
     * ViewModel properties for binding.
     */
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private ObjectProperty<Currency> currency = new SimpleObjectProperty<>();
    private BooleanProperty isNewBoard = new SimpleBooleanProperty(true);
    private ListProperty<Currency> currencies = new SimpleListProperty<>(FXCollections.observableArrayList());
    /**
     * List of participants attached to the board.
     */
    //private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();
    private ListProperty<PersonListItemViewModel> participants =  new SimpleListProperty<>(FXCollections.observableArrayList());

    private BooleanProperty participantsListEmpty = new SimpleBooleanProperty(true);
    /**
     * The list of all participants created in the application.
     * Used to populate board's participants list.
     */
//    private ObservableList<PersonListItemViewModel> persons = FXCollections.observableArrayList();
    private ListProperty<PersonListItemViewModel> persons =  new SimpleListProperty<>(FXCollections.observableArrayList());
    private BooleanProperty personsListEmpty = new SimpleBooleanProperty(true);

    /**
     * Helper field.
     * Updated whenever participants list is updated.
     */
    private boolean personListUpdated = false;

    public void initialize() {
        LOG.info("Start initialize");
        CompletableFuture.runAsync( () -> {
            LOG.debug("Load persons");
            persons.addAll(getPersonsList());
            setPersonsListEmpty(persons.size() == 0);
            LOG.debug("Found persons: " + persons.size());
        }, getRunService().getExecutorService());
        CompletableFuture.runAsync( () -> {
            currencies.addAll(CurrencyService.getAllCurrencies());
        }, getRunService().getExecutorService());
        if (scope != null && scope.getBoardModel() != null) {
            model = scope.getBoardModel();
            CompletableFuture.runAsync( () -> {
                LOG.debug("Load Participants");
                storageManager.loadParticipants(model);
                if (!model.getParticipants().isEmpty()) {
                    setParticipantsListEmpty(false);
                    participants.addAll(
                            model.getParticipants().stream()
                                    .map(PersonListItemViewModel::new)
                                    .collect(Collectors.toList())
                    );
                }
                LOG.debug("Loaded Participants: " + participants.size());
            }, getRunService().getExecutorService());
            setName(model.getName());
            setDescription(model.getDescription());
            isNewBoard.set(false);
            setCurrency(model.getCurrency());
        } else {
            model = new BoardModel();
            setCurrency(settingsService.getCurrency());
        }
        LOG.info("End initialize");
    }
    /**
     * Fetch participants from DB and convert them PersonListItemViewModel for display in a view.
     * Exclude already attached to the board participants.
     *
     * @return
     */
    private List<PersonListItemViewModel> getPersonsList() {
        return storageManager.getPersons()
                .stream()
                .filter(p -> !model.getParticipants().contains(p))
                .map(PersonListItemViewModel::new)
                .collect(Collectors.toList());
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
        setParticipantsListEmpty(false);
        return participants.add(viewModel);
    }

    /**
     * Add new participant to the list.
     * Error from here is dispatch through notification center.
     *
     * @param viewModel
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
        setParticipantsListEmpty(false);
        return participants.add(viewModel);
    }

    /**
     * @param viewModel View model passed from the View.
     * @return TRUE if removed FALSE if not or not found in the list.
     */
    public boolean removeParticipant(PersonListItemViewModel viewModel) {

        personListUpdated = true;
        boolean status = participants.remove(viewModel);
        setParticipantsListEmpty(participants.size() == 0);
        return status;
    }

    /**
     * See canGoBack() method for description.
     * @return
     */
    @Override
    public boolean isUpdated() {
        return canGoBack();
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
        final String view = scope.isHasBoards() ? "home" : "start";
        navigation.navigate(view);
    }

    /**
     * Save board data and load next view.
     */
    @Override
    public void save() {
        try {
            validate();
            boolean isCreationMode = model.getId() == null;
            model.setName(getName());
            model.setDescription(getDescription());
            model.setCurrency(getCurrency());
            LOG.info("before removing " + model.getParticipants().size());
            model.setParticipants(
                    participants.stream()
                            .map(PersonListItemViewModel::getModel)
                            .collect(Collectors.toList())
            );
            LOG.info("after updated participants " + model.getParticipants().size());

            model = storageManager.save(model, true);
            scope.setBoardModel(model);
            notificationCenter.publish(Notification.INFO, "msg.board_saved_success");
            // Can be null in unit tests.
            if (null != navigation) {
                navigation.navigate(isCreationMode ? "board-item" : "board-items");
            }
        } catch (ValidationException e) {
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        } catch (IllegalStateException e) {
            notificationCenter.publish(Notification.INFO_RAW_DISMISS, e.getMessage());
        } catch (Exception e) {
            notificationCenter.publish( Notification.INFO_DISMISS, "msg.generic_error");
        }
    }

    /*
     * Getter / Setters
     */
    public void setModel(BoardModel model) {
        this.model = model;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
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

    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public SettingsService getSettingsService() {
        return settingsService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
    public RunService getRunService() {
        return runService;
    }
    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
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

    public boolean isIsNewBoard() {
        return isNewBoard.get();
    }

    public BooleanProperty isNewBoardProperty() {
        return isNewBoard;
    }

    public void setIsNewBoard(boolean isNewBoard) {
        this.isNewBoard.set(isNewBoard);
    }


    public Currency getCurrency() {
        return currency.get();
    }

    public ObjectProperty<Currency> currencyProperty() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency.set(currency);
    }

    public ObservableList<Currency> getCurrencies() {
        return currencies.get();
    }

    public ListProperty<Currency> currenciesProperty() {
        return currencies;
    }

    public void setCurrencies(ObservableList<Currency> currencies) {
        this.currencies.set(currencies);
    }

    public ObservableList<PersonListItemViewModel> getParticipants() {
        return participants.get();
    }

    public ListProperty<PersonListItemViewModel> participantsProperty() {
        return participants;
    }


    public boolean isParticipantsListEmpty() {
        return participantsListEmpty.get();
    }

    public BooleanProperty participantsListEmptyProperty() {
        return participantsListEmpty;
    }

    public void setParticipantsListEmpty(boolean participantsListEmpty) {
        this.participantsListEmpty.set(participantsListEmpty);
    }

    public boolean isPersonsListEmpty() {
        return personsListEmpty.get();
    }

    public BooleanProperty personsListEmptyProperty() {
        return personsListEmpty;
    }

    public void setPersonsListEmpty(boolean personsListEmpty) {
        this.personsListEmpty.set(personsListEmpty);
    }

    public ObservableList<PersonListItemViewModel> getPersons() {
        return persons.get();
    }

    public ListProperty<PersonListItemViewModel> personsProperty() {
        return persons;
    }

    public void setPersons(ObservableList<PersonListItemViewModel> persons) {
        this.persons.set(persons);
    }
}
