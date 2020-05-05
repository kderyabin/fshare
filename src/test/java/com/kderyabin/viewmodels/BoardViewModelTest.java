package com.kderyabin.viewmodels;

import com.kderyabin.error.ValidationException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.StorageManager;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardViewModelTest {

    @Test
    void goBack() {

        BoardFormViewModel viewModel = new BoardFormViewModel();
//        viewModel.setStorageManager(storageManager);
        viewModel.initialize();

        assertTrue(viewModel.canGoBack(), "Must be TRUE if there were no changes in model data");

        viewModel.setName("Journey");
        viewModel.setDescription("Journey to France");
        viewModel.addParticipant("John");

        assertFalse(viewModel.canGoBack(), "Must be FALSE if data in model is changed.");
    }

    @Test
    void saveWithError() throws Exception {

        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.initialize();
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO_DISMISS, helper);
        viewModel.setNotificationCenter(notificationCenter);

        viewModel.save();

        assertEquals(1, helper.numberOfReceivedNotifications());
    }

    @Test
    void saveWithSuccess() throws Exception {
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO, helper);
        
        BoardModel model = new BoardModel();
        model.setName("Board name");
        model.setId(1);

        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.when(storageManager.save(model, true)).thenReturn(model);

        BoardScope scope = new BoardScope();

        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.setStorageManager(storageManager);
        viewModel.setScope(scope);

        viewModel.initialize();
        viewModel.setName(model.getName());
        viewModel.addParticipant("John");

        viewModel.save();

        assertEquals(1, helper.numberOfReceivedNotifications());
    }

    @Test
    void validate() {
        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.initialize();
        viewModel.setName(" \t\n");
        assertThrows(ValidationException.class, viewModel::validate, "msg.board_name_required");
        viewModel.setName("John");
        assertThrows(ValidationException.class, viewModel::validate, "msg.provide_participants");
    }

    @Test
    void addParticipantWithError() {
        BoardFormViewModel viewModel = new BoardFormViewModel();
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO_DISMISS, helper);
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.initialize();
        // Should return false and publish error message key
        assertFalse(viewModel.addParticipant("  "));
        // Is error is sent and received ?
        assertEquals(1, helper.numberOfReceivedNotifications());
    }

    @Test
    void removeParticipant() {
        // Prepare a data set
        PersonModel person = new PersonModel();
        person.setId(1);
        person.setName("John");

        BoardModel model = new BoardModel();
        model.setName("Board name");
        model.setId(1);
        model.addParticipant(person);

        BoardScope boardScope = new BoardScope();
        boardScope.setBoardModel(model);

        List<PersonModel> persons = new ArrayList<>();
        persons.add(person);
        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.when(storageManager.getPersons()).thenReturn(persons);

        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.setStorageManager(storageManager);
        viewModel.setScope(boardScope);
        viewModel.initialize();

        // Get generated list
        ObservableList <PersonListItemViewModel> personListItemView = viewModel.getParticipants();
        assertEquals(1, personListItemView.size());
        assertTrue(viewModel.removeParticipant(personListItemView.get(0)));
        assertEquals(0, personListItemView.size());
    }
}
