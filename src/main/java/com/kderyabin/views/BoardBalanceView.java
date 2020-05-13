package com.kderyabin.views;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.RefundmentModel;
import com.kderyabin.viewmodels.BoardBalanceViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;


@Component
@Scope("prototype")
public class BoardBalanceView implements FxmlView<BoardBalanceViewModel> {

    final private Logger LOG = LoggerFactory.getLogger(BoardBalanceView.class);
    @FXML
    public Label boardName;
    @FXML
    public StackedBarChart<Number, String> expensesChart;
    @FXML
    public TableView<BoardPersonTotal> statsTable;
    @FXML
    public TableView shareTables;
    @InjectViewModel
    private BoardBalanceViewModel viewModel;

    public void initialize() {
        boardName.textProperty().bind(viewModel.boardNameProperty());
        initExpensesChart();
        initStats();
        initShares();
    }

    /**
     * Set labels on expenses chart data and it to GUI.
     */
    private void initExpensesChart() {
        Map<String, XYChart.Series<Number, String>> data = viewModel.getChartData();
        if (data.size() > 0) {
            data.get("paid").setName("Paid");
            data.get("debt").setName("Debt");
            data.get("overpaid").setName("Overpaid");
            data.forEach((s, series) -> expensesChart.getData().add(series));
            return;
        }
        expensesChart.setManaged(false);
        expensesChart.setVisible(false);
    }

    /**
     * Initializes stats table.
     */
    private void initStats(){
        statsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BoardPersonTotal, String> participant = new TableColumn<>("Participant");
        participant.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<BoardPersonTotal, Number> spent = new TableColumn<>("Spent");
        spent.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<BoardPersonTotal, Number> balance = new TableColumn<>( "Balance");
        balance.setCellValueFactory(new PropertyValueFactory<>("balance"));

        TableColumn<BoardPersonTotal, Number> average = new TableColumn<>( "Average");
        average.setCellValueFactory(new PropertyValueFactory<>("boardAverage"));

        statsTable.getColumns().addAll(participant, spent, average, balance);

        statsTable.getItems().addAll(viewModel.getParticipants());
        Integer rowCount = statsTable.getItems().size() + 1;
        statsTable.setPrefHeight(rowCount * 50 + rowCount - 1);
    }

    private void initShares(){
        shareTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RefundmentModel, String> debtor = new TableColumn<>("Debtor");
        debtor.setCellValueFactory(new PropertyValueFactory<>("debtor"));

        TableColumn<RefundmentModel, String> creditor = new TableColumn<>("Creditor");
        creditor.setCellValueFactory(new PropertyValueFactory<>("creditor"));

        TableColumn<RefundmentModel, Double> amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        shareTables.getColumns().addAll( debtor, creditor, amount);
        shareTables.setItems(viewModel.getShareData());

    }
    public void addItem(ActionEvent actionEvent) throws ViewNotFoundException {
        viewModel.addItem();
    }

    public void goBack(ActionEvent actionEvent) throws ViewNotFoundException {
        viewModel.goBack();
    }
}
