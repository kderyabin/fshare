package com.kderyabin.views;

import com.kderyabin.controls.ConfirmAlert;
import com.kderyabin.viewmodels.BoardListItemViewModel;
import com.kderyabin.viewmodels.HomeViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class HomeView implements FxmlView<HomeViewModel> {

    final private static Logger LOG = LoggerFactory.getLogger(HomeView.class);

    @FXML
    public ListView<BoardListItemViewModel> boardsList;

    @InjectViewModel
    private HomeViewModel viewModel;

    @InjectResourceBundle
    private ResourceBundle resources;

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
                   editAction(vm);
                    break;
                case "removeBtn":
                    removeAction(vm);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editAction(BoardListItemViewModel vm) throws Exception {
        viewModel.edit(vm);
    }
    public void removeAction(BoardListItemViewModel vm){
        Alert alert = new ConfirmAlert(resources.getString("msg.confirm_delete_board"));
        Optional<ButtonType> option = alert.showAndWait();
        if( !option.isPresent() || option.get() != ButtonType.OK){
            return;
        }
        viewModel.remove(vm);
    }
    public void addAction() throws Exception {
        viewModel.addBoard();
    }
}
