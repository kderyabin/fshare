package com.kderyabin.mvvm.board.edit;

import com.kderyabin.error.ValidationException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import com.kderyabin.mvvm.participant.list.PersonListItemViewModel;
import com.kderyabin.services.BoardScope;
import com.kderyabin.services.RunService;
import com.kderyabin.services.SettingsService;
import com.kderyabin.services.StorageManager;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BoardEditViewModelTest {
    final private Logger LOG = LoggerFactory.getLogger(BoardEditViewModelTest.class);
    /**
     * Required dependencies for the BoardFormViewModel.
     */
    private SettingsService settingsService = new SettingsService();
    private RunService runService = new RunService(2);

    @Test
    void goBack() {

        BoardEditViewModel viewModel = new BoardEditViewModel();
        viewModel.setRunService(runService);
        viewModel.setSettingsService(settingsService);
        viewModel.initialize();

        assertTrue(viewModel.canGoBack(), "Must be TRUE if there were no changes in model data");

        viewModel.setName("Journey");
        viewModel.setDescription("Journey to France");
        viewModel.addParticipant("John");

        assertFalse(viewModel.canGoBack(), "Must be FALSE if data in model is changed.");
    }

    @Test
    void saveWithError() throws Exception {

        BoardEditViewModel viewModel = new BoardEditViewModel();
        viewModel.setRunService(runService);
        viewModel.setSettingsService(settingsService);
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
        model.setCurrency("EUR");

        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.when(storageManager.save(model, true)).thenReturn(model);

        BoardScope scope = new BoardScope();

        BoardEditViewModel viewModel = new BoardEditViewModel();
        viewModel.setRunService(runService);
        viewModel.setSettingsService(settingsService);
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
        BoardEditViewModel viewModel = new BoardEditViewModel();
        viewModel.setRunService(runService);
        viewModel.setSettingsService(settingsService);
        viewModel.initialize();
        viewModel.setName(" \t\n");
        assertThrows(ValidationException.class, viewModel::validate, "msg.board_name_required");
        viewModel.setName("John");
        assertThrows(ValidationException.class, viewModel::validate, "msg.provide_participants");
    }

    @Test
    void addParticipantWithError() {
        BoardEditViewModel viewModel = new BoardEditViewModel();
        // init dependencies
        viewModel.setSettingsService(settingsService);
        viewModel.setRunService(runService);
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
    void removeParticipant() throws InterruptedException {
        LOG.debug("Start removeParticipant test");
        // Prepare a data set
        PersonModel person = new PersonModel();
        person.setId(1);
        person.setName("John");

        PersonModel person2 = new PersonModel();
        person2.setId(2);
        person2.setName("Anna");

        // Board model for the scope
        // Board participants are not initialized
        BoardModel model = new BoardModel();
        model.setName("Board name");
        model.setId(1);
        model.setCurrency(settingsService.getCurrency());

        BoardScope boardScope = new BoardScope();
        boardScope.setBoardModel(model);

        // Mocked board model as it should be returned after call to storageManager.loadParticipants() method
        // with loaded participants
        BoardModel modelMock = new BoardModel( model.getName());
        modelMock.setId( model.getId());
        modelMock.setCurrency(model.getCurrency());
        modelMock.addParticipant(person);

        List<PersonModel> persons = new ArrayList<>();
        persons.add(person);
        persons.add(person2);

        // Mock DB access
        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.when(storageManager.getPersons()).thenReturn(persons);
        Mockito.when(storageManager.loadParticipants(model)).thenReturn(modelMock);

        BoardEditViewModel viewModel = new BoardEditViewModel();
        viewModel.setRunService(runService);
        viewModel.setSettingsService(settingsService);
        viewModel.setStorageManager(storageManager);
        viewModel.setScope(boardScope);
        viewModel.initialize();

        // need to wait until threads are terminated to do an assertion.
        ExecutorService executorService = runService.getExecutorService();
        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        LOG.info("Participants");
        LOG.info(viewModel.getParticipants().toString());
        LOG.info("Persons");
        LOG.info(viewModel.getPersons().toString());

        // Get generated list
        ObservableList<PersonListItemViewModel> personListItemView = viewModel.getParticipants();
        assertEquals(1, personListItemView.size());
        assertEquals(2, viewModel.getPersons().size());
        assertTrue(viewModel.removeParticipant(personListItemView.get(0)));
        assertEquals(0, personListItemView.size());
    }
}
