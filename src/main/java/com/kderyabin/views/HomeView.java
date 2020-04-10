package com.kderyabin.views;

import com.jfoenix.controls.JFXListView;
import com.kderyabin.viewmodels.BoardListItemViewModel;
import com.kderyabin.viewmodels.HomeViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class HomeView implements FxmlView<HomeViewModel> {
    @FXML
    public JFXListView<BoardListItemViewModel> boardsList;
    @InjectViewModel
    private HomeViewModel viewModel;

    public void initialize() {
        boardsList.setItems(viewModel.getBoardItems());
        boardsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(BoardListItemView.class));
        boardsList.addEventHandler(ActionEvent.ACTION, this::handleAction);
    }

    public void handleAction(ActionEvent event)  {
        Button btn = (Button) event.getTarget();
        BoardListItemViewModel vm = (BoardListItemViewModel) btn.getUserData();
        if(btn.getId().equals("editBtn")) {
            try {
                viewModel.edit(vm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
