package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardItemsViewModel;
import com.kderyabin.viewmodels.LinesListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BoardItemsView implements FxmlView<BoardItemsViewModel> {
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
        boardName.textProperty().bind(viewModel.boardNameProperty());
        boolean hasItems = viewModel.getLines().size() > 0;
        if (!hasItems) {
            hideElement(items);
            hideElement(chart);
            showElement(noItemsWarning);
        } else {
            items.setItems(viewModel.getLines());
            items.setCellFactory(CachedViewModelCellFactory.createForFxmlView(LinesListItemView.class));
            items.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    viewModel.editItem(newValue);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            initChart();
        }
    }
    private void initChart(){
        if(viewModel.getChartData().size() > 0) {
            viewModel.getChartData().forEach((name, amount) -> {
                String label = name + " (" + amount + ")";
                chart.getData().add( new PieChart.Data(label, amount));
            });
        }
    }
    private void hideElement(Node node){
        // prevent size calculation
        node.setManaged(false);
        // hide
        node.setVisible(false);
    }
    private void showElement(Node node){
        node.setManaged(true);
        node.setVisible(true);
    }

    public void addItem() throws Exception {
        viewModel.addItem();
    }

    public void goBack() throws Exception {
        viewModel.goBack();
    }
}
