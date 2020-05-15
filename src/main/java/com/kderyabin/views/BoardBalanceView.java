package com.kderyabin.views;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.RefundmentModel;
import com.kderyabin.viewmodels.BoardBalanceViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    public TableView<RefundmentModel> shareTables;
    @FXML
    public Label warningNoBalanceData;
    @FXML
    public VBox statsPanel;
    @FXML
    public VBox sharePanel;
    @InjectViewModel
    private BoardBalanceViewModel viewModel;

    public void initialize() {
        boardName.textProperty().bind(viewModel.boardNameProperty());
        // Display warning message if there is no data to display
        warningNoBalanceData.visibleProperty().bind(viewModel.balanceEmptyProperty());
        warningNoBalanceData.managedProperty().bind(viewModel.balanceEmptyProperty());

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
        renderVisible(expensesChart, false);
    }

    /**
     * Initializes stats table.
     */
    private void initStats() {
        // Hide container if there is no data to display
        if( viewModel.getBalanceEmpty()) {
            renderVisible(statsPanel, false);
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
    }

    private void initShares() {
        if (viewModel.getShareData().size() == 0) {
            renderVisible(sharePanel, false);
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

    public void addItem(ActionEvent actionEvent) throws ViewNotFoundException {
        viewModel.addItem();
    }

    public void goBack(ActionEvent actionEvent) throws ViewNotFoundException {
        viewModel.goBack();
    }

    /**
     * Renders a node visible or not visible.
     *
     * @param node    Element to hide/show
     * @param visible TRUE to set visible FALSE to hide
     */
    private void renderVisible(Node node, boolean visible) {
        if (!visible) {
            // prevent size calculation
            node.setManaged(false);
            // hide
            node.setVisible(false);
            return;
        }
        node.setManaged(true);
        node.setVisible(true);
    }
}
