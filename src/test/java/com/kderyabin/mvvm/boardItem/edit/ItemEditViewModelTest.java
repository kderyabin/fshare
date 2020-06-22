package com.kderyabin.mvvm.boardItem.edit;

import com.kderyabin.error.ValidationException;
import com.kderyabin.partagecore.model.BoardItemModel;
import com.kderyabin.partagecore.model.BoardModel;
import com.kderyabin.partagecore.model.PersonModel;
import com.kderyabin.mvvm.participant.list.PersonListItemViewModel;
import com.kderyabin.services.BoardScope;
import com.kderyabin.partagecore.storage.StorageManager;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemEditViewModelTest {

    private BoardScope getScope() {
        BoardScope scope = new BoardScope();
        scope.setBoardModel(null);
        scope.setItemModel(null);
        // Init test data
        PersonModel person1 = new PersonModel("John");
        person1.setId(1);
        PersonModel person2 = new PersonModel("Anna");
        person2.setId(2);

        BoardModel board = new BoardModel("Board 1");
        board.setId(1);

        board.addParticipant(person1);
        board.addParticipant(person2);
        scope.setBoardModel(board);

        return scope;
    }

    @Test
    void initialize_new() {
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(getScope());
        viewModel.initialize();
        assertEquals(2, viewModel.getParticipants().size());
    }
    @Test
    void initialize_edit() {
        BoardScope scope  = getScope();
        BoardItemModel model = new BoardItemModel();
        model.setId(1);
        model.setTitle("Title");
        model.setAmount("10.50");
        model.setBoard(scope.getBoardModel());
        model.setPerson(scope.getBoardModel().getParticipants().get(0));
        scope.setItemModel(model);

        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertEquals(viewModel.getPerson().getModel(), scope.getBoardModel().getParticipants().get(0));
        assertEquals(viewModel.getTitle(), model.getTitle());
        assertEquals(viewModel.getAmount(), model.getAmount().toString());
        assertEquals(viewModel.getDate(), model.getDate().toLocalDate());
    }


    @Test
    void canGoBack() {
        BoardScope scope  = getScope();
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertTrue(viewModel.canGoBack(), "Must be TRUE if ViewModel properties are not modified");
        viewModel.setTitle("title");
        assertFalse(viewModel.canGoBack(), "Must be FALSE if ViewModel properties are modified");
    }

    @Test
    void validate() {
        BoardScope scope  = getScope();
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
        BoardScope scope  = getScope();
        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
        NotificationTestHelper helper = new NotificationTestHelper();
        notificationCenter.subscribe(Notification.INFO, helper);

        BoardModel boardModel = scope.getBoardModel();
        BoardItemModel model = new BoardItemModel();
        model.setTitle("Title");
        model.setAmount("10.50");
        model.setBoard(boardModel);
        PersonModel person = boardModel.getParticipants().get(0);
        model.setPerson(person);

        StorageManager storageManager = Mockito.mock(StorageManager.class);
        Mockito.doNothing().when(storageManager).save(model);

        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setNotificationCenter(notificationCenter);
        viewModel.setStorageManager(storageManager);
        viewModel.setScope(scope);
        viewModel.initialize();
        viewModel.setTitle(model.getTitle());
        viewModel.setAmount(model.getAmount().toString());
        viewModel.setPerson(new PersonListItemViewModel(person));

        viewModel.save();

        assertEquals(1, helper.numberOfReceivedNotifications());
    }
    @Test
    void bigDecimalConversion(){

        Locale currentLocale = Locale.FRANCE;
        String  currencyAmount = "9,876,543.21";
        BigDecimal num = null;
        try {
            num = parse(currencyAmount, currentLocale);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Currency currentCurrency = Currency.getInstance(currentLocale);
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

        System.out.println(
                currentLocale.getDisplayName() + ", " +
                        currentCurrency.getDisplayName() + ": " +
                        currencyFormatter.format(num));

    }

    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }
}
