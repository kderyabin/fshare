package com.kderyabin.mvvm.board.balance;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.RefundmentModel;
import com.kderyabin.services.BoardScope;
import com.kderyabin.services.BoardBalance;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.RunService;
import com.kderyabin.services.StorageManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Component
@Scope("prototype")
public class BoardBalanceViewModel implements ViewModel {

    private final Logger LOG = LoggerFactory.getLogger(BoardBalanceViewModel.class);
    /*
     * Dependencies.
     */
    private RunService runService;
    private NavigateServiceInterface navigation;
    private StorageManager storageManager;
    private BoardScope boardScope;
    private BoardModel board;

    // Properties
    private BoardBalance boardBalance = new BoardBalance();
    private StringProperty boardName ;
    private StringProperty currency = new SimpleStringProperty("");
    private Map<String,  XYChart.Series<Number, String>> chartData = new LinkedHashMap<>();
    private Map<String,  Map<String,Number>> XYChartData = new LinkedHashMap<>();
    private ObservableList<RefundmentModel> shareData = FXCollections.observableArrayList();
    /**
     * Indicates that boardBalance is concurrently initialized.
     */
    private BooleanProperty balanceLoaded = new SimpleBooleanProperty(false);
    /**
     * Indicates that boardBalance is concurrently initialized.
     */
    private BooleanProperty chartLoaded = new SimpleBooleanProperty(false);
    /**
     * Indicates that boardBalance is concurrently initialized.
     */
    private BooleanProperty shareLoaded = new SimpleBooleanProperty(false);
    /**
     * Indicates if board has data to display.
     */
    private BooleanProperty balanceEmpty = new SimpleBooleanProperty(false);

    public void initialize(){
        LOG.info("Initialize");
        board = boardScope.getBoardModel();
        // Launch after board balance concurrently loaded
        CompletableFuture.runAsync(this::init, runService.getExecutorService());

        boardName = new SimpleStringProperty(board.getName());
        setCurrency(board.getCurrencyCode());
        //init();
        LOG.info("End Initialize");
    }

    public void init(){
        boardBalance.setTotals(storageManager.getBoardPersonTotal(board.getId()));
        setBalanceEmpty(boardBalance.isEmpty());
        LOG.debug("Balance size: " + boardBalance.getTotals().size());
        boardBalance.shareBoardTotal();
        setBalanceLoaded(true);
        initChartData();
        initShareData();
    }

    public void initShareData(){
        LOG.debug("+++ Start initShareData");
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
        setShareLoaded(true);
        LOG.debug("+++ End initShareData");
    }
    /**
     * Prepare chart data
     */
//    public void initChartData(){
//        LOG.debug("--- Start initChartData");
//        if(boardBalance.getShare().size() > 0) {
//            // Prepare balances for display in the chart
//            XYChart.Series<Number, String> debtData = new XYChart.Series<>();
//            XYChart.Series<Number, String> paidData = new XYChart.Series<>();
//            XYChart.Series<Number, String> overpaidData = new XYChart.Series<>();
//            // loop through balances
//            boardBalance.getBalancePerPerson().forEach( (person, balance) -> {
//                BigDecimal paid, debt, overPaid;
//
//                if (balance.compareTo(BigDecimal.ZERO) < 0) {
//                    // Negative balance: person owes moneys
//                    // convert negative balance (money owed to other participants) into spend money
//                    paid = boardBalance.getAverage().add(balance);
//                    debt = boardBalance.getAverage().subtract(paid);
//                    overPaid = new BigDecimal("0");
//                } else {
//                    paid = boardBalance.getAverage();
//                    debt = new BigDecimal("0");
//                    overPaid = balance;
//                }
//                paidData.getTotals().add(new XYChart.Data<>(paid, person.getName()));
//                debtData.getTotals().add(new XYChart.Data<>(debt, person.getName()));
//                overpaidData.getTotals().add(new XYChart.Data<>(overPaid, person.getName()));
//            });
//            chartData.put("paid", paidData);
//            chartData.put("debt", debtData);
//            chartData.put("overpaid", overpaidData);
//        }
//        setChartLoaded(true);
//        LOG.debug("--- End initChartData");
//    }
    public void initChartData(){
        LOG.debug("--- Start initXYChartData");
        if(!boardBalance.isEmpty()) {
            // Prepare balances for display in the chart
            BigDecimal average = boardBalance.getAverage();
            // loop through balances
            boardBalance.getBalancePerPerson().forEach( (person, balance) -> {
                BigDecimal paid, debt, overpaid;
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    // Negative balance: person owes moneys
                    // convert negative balance (money owed to other participants) into spend money
                    paid = average.add(balance);
                    debt = average.subtract(paid);
                    overpaid = new BigDecimal("0");
                } else {
                    paid = average;
                    debt = new BigDecimal("0");
                    overpaid = balance;
                }
                Map<String, Number> personStats = new HashMap<>();
                personStats.put("paid", paid);
                personStats.put("debt", debt);
                personStats.put("overpaid", overpaid);
                XYChartData.put(person.getName(), personStats);
            });
        }
        setChartLoaded(true);
        LOG.debug("--- End initXYChartData");
    }
    public void goBack() {
        navigation.navigate("board-items");
    }

    public void addItem() {
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
        return boardBalance.getTotals();
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

    public String getCurrency() {
        return currency.get();
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public RunService getRunService() {
        return runService;
    }
    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public boolean isBalanceLoaded() {
        return balanceLoaded.get();
    }

    public BooleanProperty balanceLoadedProperty() {
        return balanceLoaded;
    }

    public void setBalanceLoaded(boolean balanceLoaded) {
        this.balanceLoaded.set(balanceLoaded);
    }

    public boolean isChartLoaded() {
        return chartLoaded.get();
    }

    public BooleanProperty chartLoadedProperty() {
        return chartLoaded;
    }

    public void setChartLoaded(boolean chartLoaded) {
        this.chartLoaded.set(chartLoaded);
    }

    public boolean  isShareLoaded() {
        return shareLoaded.get();
    }

    public BooleanProperty shareLoadedProperty() {
        return shareLoaded;
    }

    public void setShareLoaded(boolean shareLoaded) {
        this.shareLoaded.set(shareLoaded);
    }

    public Map<String, Map<String, Number>> getXYChartData() {
        return XYChartData;
    }

    public void setXYChartData(Map<String, Map<String, Number>> XYChartData) {
        this.XYChartData = XYChartData;
    }
}
