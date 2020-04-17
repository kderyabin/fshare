package com.kderyabin.viewmodels;

import com.kderyabin.error.ValidationException;
import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.repository.BoardItemRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemEditViewModelTest {

    final private BoardScope scope = new BoardScope();

    @BeforeAll
    public void setUp() {
        // Init test data
        PersonModel person1 = new PersonModel("John");
        person1.setId(1);
        PersonModel person2 = new PersonModel("Anna");
        person2.setId(2);

        BoardModel board = new BoardModel("Board 1");
        board.setId(1);

        board.addParticipant(person1);
        board.addParticipant(person2);
        scope.setModel(board);
    }

    @Test
    void initialize() {
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertEquals(2, viewModel.getParticipants().size());
    }


    @Test
    void canGoBack() {
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertTrue(viewModel.canGoBack());
        viewModel.setTitle("title");
        assertFalse(viewModel.canGoBack());
    }

    @Test
    void validate() {
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertThrows(ValidationException.class, viewModel::validate, "msg.title_is_required");
        viewModel.setTitle("title");
        assertThrows(ValidationException.class, viewModel::validate, "msg.amount_is_required");
        viewModel.setAmount("10");
        assertThrows(ValidationException.class, viewModel::validate, "msg.person_is_required");
    }

    @Test
    void saveWithSuccess() throws Exception {
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO, helper);

        BoardModel boardModel = scope.getModel();
        BoardItemModel model = new BoardItemModel();
        model.setTitle("Title");
        model.setAmount("10.50");
        model.setBoard(boardModel);
        PersonModel person = boardModel.getParticipants().stream().findFirst().get();
        model.setPerson(person);

        BoardItemRepository repository = Mockito.mock(BoardItemRepository.class);
        Mockito.when(repository.save(model)).thenReturn(model);

        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.setRepository(repository);
        viewModel.setScope(scope);
        viewModel.initialize();
        viewModel.setTitle(model.getTitle());
        viewModel.setAmount(model.getAmount().toString());
        viewModel.setPerson(new PersonListItemViewModel(person));

        viewModel.save();

        assertEquals(1, helper.numberOfReceivedNotifications());
    }
}
