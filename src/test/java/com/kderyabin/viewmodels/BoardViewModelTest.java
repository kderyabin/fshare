package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.util.Notification;
import com.kderyabin.views.PersonListItemView;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationObserver;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardViewModelTest {

    @Test
    void goBack() {
        BoardViewModel model = new BoardViewModel();
        model.initialize();

        assertTrue(model.canGoBack(), "Must be TRUE if there were no changes in model data");

        model.setName("Journey");
        model.setDescription("Journey to France");
        model.addParticipant("John");

        assertFalse(model.canGoBack(), "Must be FALSE if data in model is changed.");
    }

    @Test
    void saveWithError() throws Exception {
        BoardViewModel viewModel = new BoardViewModel();
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

        BoardViewModel viewModel = new BoardViewModel();
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
        BoardViewModel viewModel = new BoardViewModel();
        viewModel.initialize();
        viewModel.setName(" \t\n");
        assertThrows(ValidationException.class, viewModel::validate, "msg.board_name_required");
        viewModel.setName("John");
        assertThrows(ValidationException.class, viewModel::validate, "msg.provide_participant");
    }

    @Test
    void addParticipantWithError() {
        BoardViewModel viewModel = new BoardViewModel();
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO_DISMISS, helper);
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.initialize();
        assertFalse(viewModel.addParticipant("  "));
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

        BoardViewModel viewModel = new BoardViewModel();
        viewModel.setModel(model);
        viewModel.initialize();

        // Get generated list
        ObservableList <PersonListItemViewModel> personListItemView = viewModel.getParticipants();
        assertEquals(1, personListItemView.size());
        assertTrue(viewModel.removeParticipant(personListItemView.get(0)));
        assertEquals(0, personListItemView.size());
    }
}
