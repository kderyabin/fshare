package com.kderyabin.views;

import com.kderyabin.util.GUIHelper;
import com.kderyabin.viewmodels.BoardItemsViewModel;
import com.kderyabin.viewmodels.LinesListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BoardItemsView implements FxmlView<BoardItemsViewModel> {

    final private static Logger LOG = LoggerFactory.getLogger(BoardItemsView.class);

    @FXML
    public Label boardName;
    @FXML
    public ListView<LinesListItemViewModel> items;
    @FXML
    public Label noItemsWarning;
    @FXML
    public PieChart chart;

    @InjectViewModel
    private BoardItemsViewModel viewModel;

    public void initialize() {
        Platform.runLater(() -> {
            LOG.debug("Initialize");
            boardName.textProperty().bind(viewModel.boardNameProperty());

            items.setCellFactory(CachedViewModelCellFactory.createForFxmlView(LinesListItemView.class));
            items.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> viewModel.editItem(newValue));
            items.itemsProperty().bind(viewModel.linesProperty());

            if(viewModel.getChartLoaded()) {
                LOG.debug("Chart data is loaded");
                // Chart's data is loaded in another thread.
                // If it's already loaded initialize the PieChart.
                initChartData();
            } else {
                // Chart's data is not ready yet.
                // Attach listener so the PieChart will be initialized once the data is loaded.
                viewModel.chartLoadedProperty().addListener(this::chartLoadedPropertyListener);
            }
            // Special display for the empty board.
            if(viewModel.isLinesLoaded()){
                LOG.debug("Lines are loaded");
                manageEmptyBoardDisplay();
            } else {
                // Lines are not loaded yet.
                // Attache listener and do the job once they are loaded
                viewModel.linesLoadedProperty().addListener(this::linesLoadedPropertyListener);
            }
            LOG.debug("End initialize");
        });
    }

    /**
     * PieChart initialize thru the listener.
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void chartLoadedPropertyListener(Observable observable, Boolean oldValue, Boolean newValue) {
        LOG.debug("In chartLoadedPropertyListener");
        if(newValue) {
            initChartData();
        }
    }

    /**
     * Manager empty board as listener.
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void linesLoadedPropertyListener(Observable observable, Boolean oldValue, Boolean newValue) {
        LOG.debug("In linesLoadedPropertyListener");
        if(newValue) {
            manageEmptyBoardDisplay();
        }
    }

    /**
     * If board has no items yet (case of newly created board)
     * hide blocks with chart and lines abd display an appropriate message.
     * It checks the number of loaded lines in the ViewModel so it MUST be ran after the model is initialized.
     * The method must be run on GUI thread.
     */
    public void manageEmptyBoardDisplay(){
        Platform.runLater( () -> {
            if (viewModel.getLines().isEmpty()) {
                GUIHelper.renderVisible(items, false);
                GUIHelper.renderVisible(chart, false);
                GUIHelper.renderVisible(noItemsWarning, true);
            }
        });
    }
    /**
     * Converts model's data into PieChart.Data.
     * The method must be run on GUI thread.
     */
    private void initChartData(){
        Platform.runLater( () -> {
            viewModel.getChartData().forEach((key, value) -> {
                PieChart.Data data = new PieChart.Data(key, value);
                chart.getData().add(data);
            });
        });
    }

    public void addItem() throws Exception {
        viewModel.addItem();
    }

    public void goBack() throws Exception {
        viewModel.goBack();
    }

    public void viewBalance(ActionEvent actionEvent) {
        viewModel.goToBalance();
    }
}
