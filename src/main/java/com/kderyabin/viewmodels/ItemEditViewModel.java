package com.kderyabin.viewmodels;

import com.kderyabin.error.ValidationException;
import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.StorageManager;
import com.kderyabin.storage.entity.BoardItemEntity;
import com.kderyabin.storage.repository.BoardItemRepository;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Component
@Scope("prototype")
@Transactional
public class ItemEditViewModel implements ViewModel {
    private static Logger LOG = LoggerFactory.getLogger(ItemEditViewModel.class);

    private StorageManager storageManager;
    private NotificationCenter notificationCenter;
    private NavigateServiceInterface navigation;
    private BoardItemEntity model;
    private BoardScope scope;

    // Properties
    private StringProperty title = new SimpleStringProperty("");
    private StringProperty amount = new SimpleStringProperty("");
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
    private ObjectProperty<PersonListItemViewModel> person = new SimpleObjectProperty<>();
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();


    public void initialize() {
        Set<PersonEntity> persons = scope.getBoardModel().getParticipants();
        participants.addAll(
                persons.stream()
                        .map(PersonListItemViewModel::new)
                        .collect(Collectors.toList())
        );
        if (scope.getItemModel() != null) {
            LOG.debug("Item model found in scope");
            model = scope.getItemModel();
            setTitle(model.getTitle());
            setAmount(model.getAmount().toString());
            setDate(model.getDate().toLocalDate());
            // Find generated item viewmodel
            participants.stream().filter(item -> item.getModel().equals(model.getPerson())).findFirst().ifPresent(this::setPerson);
        } else {
            model = new BoardItemEntity();
            model.setBoard(scope.getBoardModel());
        }
    }

    public void goBack() throws ViewNotFoundException {
        if (navigation != null) {
            navigation.navigate("board-items");
        }

    }

    /**
     * Checks if properties has been updated.
     *
     * @return TRUE if no changes were made otherwise FALSE
     */
    public boolean canGoBack() {
        if (model.getId() == null) {
            return getTitle().trim().isEmpty() &&
                    getAmount().trim().isEmpty() &&
                    getPerson() == null;
        }
        return getTitle().equals(model.getTitle()) &&
                getAmount().equals(model.getAmount().toString()) &&
                getDate().equals(model.getDate().toLocalDate()) &&
                getPerson().getModel().equals(model.getPerson());
    }

    public void validate() throws ValidationException {
        if (getTitle().trim().isEmpty()) {
            throw new ValidationException("msg.title_is_required");
        }
        if (getAmount().trim().isEmpty()) {
            throw new ValidationException("msg.amount_is_required");
        }
        if (getPerson() == null) {
            throw new ValidationException("msg.person_is_required");
        }
    }

    /**
     * Save board item and load next view.
     */
    @Transactional
    public void save() throws ViewNotFoundException {
        try {
            validate();

//            model.setTitle(getTitle());
//            model.setAmount(getAmount());
//            model.setPerson(person.getValue().getModel());

//            model = repository.save(model);
            notificationCenter.publish(Notification.INFO, "msg.item_saved_success");
            // Can be null in unit tests.
            if (null != navigation) {
                navigation.navigate("board-items");
            }
        } catch (ValidationException e) {
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        }
    }

    /**
     * Converts string into BigDecimal according to current locale.
     *
     * @param amount Amount.
     * @param locale Current locale.
     * @return
     * @throws ParseException
     */
    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
    }

    /*
     * Getters/ Setters
     */

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

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    @Autowired
    public void setRepository(BoardItemRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
}
