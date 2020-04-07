package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

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
    void save() {
        BoardViewModel model = new BoardViewModel();
        model.initialize();
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO_DISMISS, helper);
        model.setNotificationCenter(notificationCenter);

        model.save();

        assertEquals(1, helper.numberOfReceivedNotifications());

        helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO, helper);
        BoardRepository repository = Mockito.mock(BoardRepository.class);
        model.setRepository(repository);

        Mockito.when(repository.save()).thenReturn(model);

    }
}
