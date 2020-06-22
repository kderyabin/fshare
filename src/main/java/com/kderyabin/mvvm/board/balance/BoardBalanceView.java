package com.kderyabin.mvvm.board.balance;

import com.kderyabin.partagecore.model.BoardPersonTotal;
import com.kderyabin.partagecore.model.RefundmentModel;
import com.kderyabin.util.GUIHelper;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;


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
    public TableView<RefundmentModel> shareTables;
    @FXML
    public Label warningNoBalanceData;
    @FXML
    public VBox statsPanel;
    @FXML
    public VBox sharePanel;
    @FXML
    public Text balanceCurrency;
    @FXML
    public Text refundmentCurrency;
    @InjectViewModel
    private BoardBalanceViewModel viewModel;
    @InjectResourceBundle
    private ResourceBundle resource;

    public void initialize() {
//        Platform.runLater(() -> {
        LOG.info(">>> Start initialize");
        boardName.textProperty().bind(viewModel.boardNameProperty());
        balanceCurrency.textProperty().set(
                String.format("(%s : %s)",
                        resource.getString("currency"),
                        viewModel.getCurrency()
                )
        );
        // Same currency and label for refundement table
        refundmentCurrency.textProperty().bind(balanceCurrency.textProperty());

        if (viewModel.isBalanceLoaded()) {
            LOG.debug(" balanceLoaded  loaded");
            // Display warning message if there is no data to display
            warningNoBalanceData.visibleProperty().bind(viewModel.balanceEmptyProperty());
            warningNoBalanceData.managedProperty().bind(viewModel.balanceEmptyProperty());
            initStats();
        } else {
            viewModel.balanceLoadedProperty().addListener((observable, oldValue, newValue) -> {
                LOG.debug("In balanceLoaded listener");
                warningNoBalanceData.visibleProperty().bind(viewModel.balanceEmptyProperty());
                warningNoBalanceData.managedProperty().bind(viewModel.balanceEmptyProperty());
                initStats();
            });
        }

        if (viewModel.isChartLoaded()) {
            LOG.debug("isChartLoaded  loaded");
            initExpensesChart();
        } else {
            viewModel.chartLoadedProperty().addListener((observable, oldValue, newValue) -> {
                LOG.debug("listener  isChartLoaded  loaded");
                initExpensesChart();
            });
        }
        if (viewModel.isShareLoaded()) {
            LOG.debug("isShareLoaded  loaded");
            initShares();
        } else {
            viewModel.shareLoadedProperty().addListener((observable, oldValue, newValue) -> {
                LOG.debug("shareLoadedProperty listener  loaded");
                initShares();
            });
        }

        LOG.info(">>> End initialize");
//        });
    }

    /**
     * Set labels on expenses chart data and it to GUI.
     */
    private void initExpensesChart() {
        Platform.runLater(() -> {

            if (viewModel.getXYChartData().size() == 0) {
                GUIHelper.renderVisible(expensesChart, false);
                return;
            }
            // Prepare balances for display in the chart
            XYChart.Series<Number, String> debtSeries = new XYChart.Series<>();
            XYChart.Series<Number, String> paidSeries = new XYChart.Series<>();
            XYChart.Series<Number, String> overpaidSeries = new XYChart.Series<>();
            debtSeries.setName(resource.getString("debt"));
            paidSeries.setName(resource.getString("paid"));
            overpaidSeries.setName(resource.getString("overpaid"));

            // Convert raw data into XYChart.Series
            viewModel.getXYChartData().forEach((personName, typeAmount) -> {
                paidSeries.getData().add(new XYChart.Data<>(typeAmount.get("paid"), personName));
                debtSeries.getData().add(new XYChart.Data<>(typeAmount.get("debt"), personName));
                overpaidSeries.getData().add(new XYChart.Data<>(typeAmount.get("overpaid"), personName));
            });
            expensesChart.getData().add(paidSeries);
            expensesChart.getData().add(debtSeries);
            expensesChart.getData().add(overpaidSeries);
        });
    }

    /**
     * Initializes stats table.
     */
    private void initStats() {
        Platform.runLater(() -> {
            // Hide container if there is no data to display
            if (viewModel.getBalanceEmpty()) {
                GUIHelper.renderVisible(statsPanel, false);
                return;
            }
            statsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<BoardPersonTotal, String> participant = new TableColumn<>("Participant");
            participant.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<BoardPersonTotal, Number> spent = new TableColumn<>("Spent");
            spent.setCellValueFactory(new PropertyValueFactory<>("total"));

            TableColumn<BoardPersonTotal, Number> balance = new TableColumn<>("Balance");
            balance.setCellValueFactory(new PropertyValueFactory<>("balance"));

            TableColumn<BoardPersonTotal, Number> average = new TableColumn<>("Average");
            average.setCellValueFactory(new PropertyValueFactory<>("boardAverage"));

            statsTable.getColumns().addAll(participant, spent, average, balance);

            statsTable.getItems().addAll(viewModel.getParticipants());
            statsTable.setPrefHeight(getTableHeight(statsTable.getItems().size()));
        });
    }

    private void initShares() {
        Platform.runLater(() -> {
            if (viewModel.getShareData().size() == 0) {
                GUIHelper.renderVisible(sharePanel, false);
            }
            shareTables.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            TableColumn<RefundmentModel, String> debtor = new TableColumn<>("Debtor");
            debtor.setCellValueFactory(new PropertyValueFactory<>("debtor"));

            TableColumn<RefundmentModel, String> creditor = new TableColumn<>("Creditor");
            creditor.setCellValueFactory(new PropertyValueFactory<>("creditor"));

            TableColumn<RefundmentModel, Double> amount = new TableColumn<>("Amount");
            amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

            shareTables.getColumns().addAll(debtor, creditor, amount);
            shareTables.setItems(viewModel.getShareData());

            shareTables.setPrefHeight(getTableHeight(shareTables.getItems().size()));
        });
    }

    /**
     * Calculate the height of the table
     *
     * @param rowCount Number if items in the TableView (will be increment to count header row as well)
     * @return
     */
    private int getTableHeight(int rowCount) {
        int rowHeight = 50;
        return (++rowCount * rowHeight) + rowCount - 1;
    }

    public void addItem(ActionEvent actionEvent) {
        viewModel.addItem();
    }

    public void goBack(ActionEvent actionEvent) {
        viewModel.goBack();
    }
}
