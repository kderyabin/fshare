package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardItemsViewModel;
import com.kderyabin.viewmodels.LinesListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    @InjectViewModel
    private BoardItemsViewModel viewModel;

    public void initialize() {
        boardName.textProperty().bind(viewModel.boardNameProperty());
        items.setItems(viewModel.getLines());
        items.setCellFactory(CachedViewModelCellFactory.createForFxmlView(LinesListItemView.class));
        items.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                viewModel.editItem(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void addItem(ActionEvent actionEvent) throws Exception {
        viewModel.addItem();
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        viewModel.goBack();
    }
}
