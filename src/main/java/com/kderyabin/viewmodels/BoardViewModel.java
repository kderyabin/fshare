package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class BoardViewModel implements ViewModel {

    private NavigateServiceInterface navigation;
    @InjectResourceBundle
    ResourceBundle resources;
    NotificationCenter notificationCenter;
    private BoardRepository repository;

    private BoardModel model;
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty description = new SimpleStringProperty("");
    private ObservableList<PersonListItemViewModel> participants = FXCollections.observableArrayList();
    private boolean personListUpdated = false;

    public void initialize() {
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
     *
     * @param name Participant name.
     * @return TRUE on success False en failure.
     */
    public boolean addParticipant(String name) {
        if (name.isEmpty()) {
            notificationCenter.publish(
                    Notification.INFO_DISMISS, resources.getString("msg.partipant_name_required")
            );
            return false;
        }
        PersonModel personModel = new PersonModel();
        personModel.setName(name);

        PersonListItemViewModel viewModel = new PersonListItemViewModel(personModel);
        personListUpdated = true;
        return participants.add(viewModel);
    }

    public void removeParticipant(PersonListItemViewModel personListItemViewModel) {
        personListUpdated = true;
        participants.remove(personListItemViewModel);
    }

    /**
     * Check if model data has been updated.
     *
     * @return
     */
    private boolean isUpdated() {
        boolean status = getName().equals(model.getName()) &&
                getDescription().equals(model.getDescription()) &&
                !personListUpdated;

        return !status;
    }

    /**
     * Validate data integrity.
     *
     * @throws ValidationException
     */
    public void validate() throws ValidationException {
        if (getName().isEmpty()) {
            throw new ValidationException(
                    resources.getString("msg.board_name_required")
            );
        }

        if (participants.size() == 0) {
            throw new ValidationException(
                    resources.getString("msg.provide_participant")
            );
        }
    }

    public boolean goBack() {
        try {
            if (isUpdated()) {
                return false;
            }

            navigation.navigate("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Save board data.
     */
    public void save() {
        try {
            validate();

            model.setName(getName());
            model.setDescription(getDescription());
            model.initUpdateTime();
            model.getParticipants().clear();
            participants.forEach(person -> model.addParticipant(person.getModel()));

            model = repository.save(model);
            notificationCenter.publish(Notification.INFO, "msg.board_saved_success");
            navigation.navigate("start");
        } catch (ValidationException e) {
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            notificationCenter.publish(Notification.INFO_DISMISS, e.getMessage());
        }
    }
}
