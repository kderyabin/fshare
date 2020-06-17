package com.kderyabin.mvvm.home;

import com.kderyabin.controls.ConfirmAlert;
import com.kderyabin.mvvm.home.list.BoardListItemViewModel;
import com.kderyabin.mvvm.home.list.BoardListItemView;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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
//        Platform.runLater(() -> {
            LOG.debug("Started initialize");
            boardsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(BoardListItemView.class));
            boardsList.addEventHandler(ActionEvent.ACTION, this::handleBtnClick);
            boardsList.getSelectionModel().selectedItemProperty().addListener(this::handleItemClick);
            boardsList.itemsProperty().bind(viewModel.boardItemsProperty());
            LOG.debug("End initialize");
//        });
    }

    /**
     * Action to display board's details.
     */
    protected void handleItemClick(
            ObservableValue<? extends BoardListItemViewModel> observable,
            BoardListItemViewModel oldValue,
            BoardListItemViewModel newValue
    ) {
        try {
            viewModel.viewList(newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handle click on a button.
     *
     * @param event Event instance.
     */
    public void handleBtnClick(ActionEvent event) {
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

    /**
     * Action to trigger board edition.
     *
     * @param vm Model.
     */
    public void editAction(BoardListItemViewModel vm) {
        viewModel.edit(vm);
    }

    /**
     * Remove board action.
     *
     * @param vm Board
     */
    public void removeAction(BoardListItemViewModel vm) {
        Alert alert = new ConfirmAlert(resources.getString("msg.confirm_delete_board"));
        Optional<ButtonType> option = alert.showAndWait();
        if (!option.isPresent() || option.get() != ButtonType.OK) {
            return;
        }
        if (viewModel.remove(vm)) {
            // adjust list's display otherwise the empty line is displayed.
            boardsList.layout();
        }
    }

    /**
     * Add new board action.
     */
    public void addAction() {
        viewModel.addBoard();
    }
}
