package com.kderyabin.viewmodels;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.RefundmentModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.BoardBalance;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.StorageManager;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
@Scope("prototype")
public class BoardBalanceViewModel implements ViewModel {

    private final Logger LOG = LoggerFactory.getLogger(BoardBalanceViewModel.class);

    private NavigateServiceInterface navigation;
    private StorageManager storageManager;
    private BoardScope boardScope;
    private BoardBalance boardBalance = new BoardBalance();
    private BoardModel board;

    // Properties
    private StringProperty boardName;
    private Map<String,  XYChart.Series<Number, String>> chartData = new LinkedHashMap<>();
    private ObservableList<RefundmentModel> shareData = FXCollections.observableArrayList();
    /**
     * Indicates id board has data to display.
     */
    private BooleanProperty balanceEmpty = new SimpleBooleanProperty(false);

    public void initialize(){
        board = boardScope.getBoardModel();
        LOG.info("Balance for Board: " + board.getId());
        boardName = new SimpleStringProperty(board.getName());
        init();
    }

    public void init(){
        boardBalance.setData(storageManager.getBoardPersonTotal(board.getId()));
        setBalanceEmpty(boardBalance.isEmpty());
        LOG.info("Balance size: " + boardBalance.getData().size());
        boardBalance.shareBoardTotal();
        initChartData();
        initShareData();
    }

    public void initShareData(){
        final Currency currency = board.getCurrency();
        boardBalance.getShare().forEach( (debtor, data) -> {
            // Filter if there is an amount to refund
            data.forEach((personModel, decimal) -> {
                if (decimal.compareTo(new BigDecimal("0")) > 0) {

                    RefundmentModel model = new RefundmentModel();
                    model.setAmount(decimal.doubleValue());
                    model.setCreditor(personModel.getName());
                    model.setDebtor(debtor.getName());
                    model.setCurrency(currency);

                    shareData.add(model);
                }
            });
        });
    }
    /**
     * Prepare chart data
     */
    public void initChartData(){
        ObservableMap<String,  XYChart.Series<Number, String>> k;
        if(boardBalance.getShare().size() > 0) {
            // Prepare balances for display in the chart
            XYChart.Series<Number, String> debtData = new XYChart.Series<>();
            XYChart.Series<Number, String> paidData = new XYChart.Series<>();
            XYChart.Series<Number, String> overpaidData = new XYChart.Series<>();
            // loop through balances
            boardBalance.getBalancePerPerson().forEach( (person, balance) -> {
                BigDecimal paid, debt, overPaid;

                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    // Negative balance: person owes moneys
                    // convert negative balance (money owed to other participants) into spend money
                    paid = boardBalance.getAverage().add(balance);
                    debt = boardBalance.getAverage().subtract(paid);
                    overPaid = new BigDecimal("0");
                } else {
                    paid = boardBalance.getAverage();
                    debt = new BigDecimal("0");
                    overPaid = balance;
                }
                paidData.getData().add(new XYChart.Data<>(paid, person.getName()));
                debtData.getData().add(new XYChart.Data<>(debt, person.getName()));
                overpaidData.getData().add(new XYChart.Data<>(overPaid, person.getName()));
            });
            chartData.put("paid", paidData);
            chartData.put("debt", debtData);
            chartData.put("overpaid", overpaidData);
        }
    }

    public void goBack() throws ViewNotFoundException {
        navigation.navigate("board-items");
    }

    public void addItem() throws ViewNotFoundException {
        navigation.navigate("board-item");
    }


    /*
     * Getters / Setters
     */

    public ObservableList<RefundmentModel> getShareData() {
        return shareData;
    }

    public StringProperty boardNameProperty() {
        return boardName;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public BoardScope getBoardScope() {
        return boardScope;
    }
    @Autowired
    public void setBoardScope(BoardScope boardScope) {
        this.boardScope = boardScope;
    }

    public BoardBalance getBoardBalance() {
        return boardBalance;
    }

    public void setBoardBalance(BoardBalance boardBalance) {
        this.boardBalance = boardBalance;
    }

    public Map<String, XYChart.Series<Number, String>> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, XYChart.Series<Number, String>> chartData) {
        this.chartData = chartData;
    }

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }
    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public List<BoardPersonTotal> getParticipants(){
        return boardBalance.getData();
    }

    public boolean getBalanceEmpty() {
        return balanceEmpty.get();
    }

    public BooleanProperty balanceEmptyProperty() {
        return balanceEmpty;
    }

    public void setBalanceEmpty(boolean balanceEmpty) {
        this.balanceEmpty.set(balanceEmpty);
    }
}
