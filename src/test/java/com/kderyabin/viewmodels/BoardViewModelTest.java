package com.kderyabin.viewmodels;

import com.kderyabin.repository.BoardRepository;
import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BoardViewModelTest {

    @Test
    void goBack() {
        BoardFormViewModel model = new BoardFormViewModel();
        model.initialize();

        assertTrue(model.canGoBack(), "Must be TRUE if there were no changes in model data");

        model.setName("Journey");
        model.setDescription("Journey to France");
        model.addParticipant("John");

        assertFalse(model.canGoBack(), "Must be FALSE if data in model is changed.");
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

        BoardRepository repository = Mockito.mock(BoardRepository.class);
        Mockito.when(repository.save(model)).thenReturn(model);

        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.setRepository(repository);
        viewModel.setModel(model);
        viewModel.initialize();
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

        BoardFormViewModel viewModel = new BoardFormViewModel();
        viewModel.setModel(model);
        viewModel.initialize();

        // Get generated list
        ObservableList <PersonListItemViewModel> personListItemView = viewModel.getParticipants();
        assertEquals(1, personListItemView.size());
        assertTrue(viewModel.removeParticipant(personListItemView.get(0)));
        assertEquals(0, personListItemView.size());
    }
}
