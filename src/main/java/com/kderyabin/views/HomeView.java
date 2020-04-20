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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.event.MouseEvent;

@Component
@Scope("prototype")
public class HomeView implements FxmlView<HomeViewModel> {

    final private static Logger LOG = LoggerFactory.getLogger(HomeView.class);

    @FXML
    public ListView<BoardListItemViewModel> boardsList;
    @InjectViewModel
    private HomeViewModel viewModel;
    Long pressDelay = 0L;

    public void initialize() {
        LOG.info(">>> Started initialisation");

        boardsList.setItems(viewModel.getBoardItems());
        boardsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(BoardListItemView.class));
        boardsList.addEventHandler(ActionEvent.ACTION, this::handleAction);

        boardsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                viewModel.viewList(newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        LOG.info(">>> Ended initialisation");
    }

    public void handleAction(ActionEvent event) {
        Button btn = (Button) event.getTarget();
        BoardListItemViewModel vm = (BoardListItemViewModel) btn.getUserData();
        try {
            switch (btn.getId()) {
                case "editBtn":
                    viewModel.edit(vm);
                    break;
                case "removeBtn":
                    viewModel.remove(vm);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(ActionEvent actionEvent) throws Exception {
        viewModel.addBoard();
    }
}
