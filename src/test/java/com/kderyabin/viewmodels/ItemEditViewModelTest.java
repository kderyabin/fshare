package com.kderyabin.viewmodels;

import com.kderyabin.error.ValidationException;
import com.kderyabin.storage.entity.BoardItemEntity;
import com.kderyabin.storage.entity.BoardEntity;
import com.kderyabin.storage.entity.PersonEntity;
import com.kderyabin.storage.repository.BoardItemRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import de.saxsys.mvvmfx.utils.notifications.NotificationTestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemEditViewModelTest {

    final private BoardScope scope = new BoardScope();
//
//    @BeforeAll
//    public void setUp() {
//        // Init test data
//        PersonEntity person1 = new PersonEntity("John");
//        person1.setId(1);
//        PersonEntity person2 = new PersonEntity("Anna");
//        person2.setId(2);
//
//        BoardEntity board = new BoardEntity("Board 1");
//        board.setId(1);
//
//        board.addParticipant(person1);
//        board.addParticipant(person2);
//        scope.setBoardModel(board);
//    }
//
//    @Test
//    void initialize() {
//        ItemEditViewModel viewModel = new ItemEditViewModel();
//        viewModel.setScope(scope);
//        viewModel.initialize();
//        assertEquals(2, viewModel.getParticipants().size());
//    }
//
//
//    @Test
//    void canGoBack() {
//        ItemEditViewModel viewModel = new ItemEditViewModel();
//        viewModel.setScope(scope);
//        viewModel.initialize();
//        assertTrue(viewModel.canGoBack());
//        viewModel.setTitle("title");
//        assertFalse(viewModel.canGoBack());
//    }
//
//    @Test
//    void validate() {
//        ItemEditViewModel viewModel = new ItemEditViewModel();
//        viewModel.setScope(scope);
//        viewModel.initialize();
//        assertThrows(ValidationException.class, viewModel::validate, "msg.title_is_required");
//        viewModel.setTitle("title");
//        assertThrows(ValidationException.class, viewModel::validate, "msg.amount_is_required");
//        viewModel.setAmount("10");
//        assertThrows(ValidationException.class, viewModel::validate, "msg.person_is_required");
//    }
//
//    @Test
//    void saveWithSuccess() throws Exception {
//        NotificationCenter notificationCenter = MvvmFX.getNotificationCenter();
//        NotificationTestHelper helper = new NotificationTestHelper();
//        notificationCenter.subscribe(Notification.INFO, helper);
//
//        BoardEntity boardModel = scope.getBoardModel();
//        BoardItemEntity model = new BoardItemEntity();
//        model.setTitle("Title");
//        model.setAmount("10.50");
//        model.setBoard(boardModel);
//        PersonEntity person = boardModel.getParticipants().stream().findFirst().get();
//        model.setPerson(person);
//
//        BoardItemRepository repository = Mockito.mock(BoardItemRepository.class);
//        Mockito.when(repository.save(model)).thenReturn(model);
//
//        ItemEditViewModel viewModel = new ItemEditViewModel();
//        viewModel.setNotificationCenter(notificationCenter);
//        viewModel.setRepository(repository);
//        viewModel.setScope(scope);
//        viewModel.initialize();
//        viewModel.setTitle(model.getTitle());
//        viewModel.setAmount(model.getAmount().toString());
//        viewModel.setPerson(new PersonListItemViewModel(person));
//
//        viewModel.save();
//
//        assertEquals(1, helper.numberOfReceivedNotifications());
//    }
//    @Test
//    void bigDecimalConversion(){
//        Locale currentLocale = Locale.FRANCE;
//        String  currencyAmount = "9,876,543.21";
//        BigDecimal num = null;
//        try {
//            num = parse(currencyAmount, currentLocale);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Currency currentCurrency = Currency.getInstance(currentLocale);
//        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);
//
//        System.out.println(
//                currentLocale.getDisplayName() + ", " +
//                        currentCurrency.getDisplayName() + ": " +
//                        currencyFormatter.format(num));
//
//    }
//
//    public static BigDecimal parse(final String amount, final Locale locale) throws ParseException, ParseException {
//        final NumberFormat format = NumberFormat.getNumberInstance(locale);
//        if (format instanceof DecimalFormat) {
//            ((DecimalFormat) format).setParseBigDecimal(true);
//        }
//        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
//    }
}
