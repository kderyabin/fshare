package com.kderyabin.viewmodels;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.StorageManager;
import com.kderyabin.storage.repository.BoardRepository;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@ScopeProvider(scopes = BoardScope.class)
public class HomeViewModel implements ViewModel {
    final private Logger LOG = LoggerFactory.getLogger(HomeViewModel.class);
    /*
     *  Dependencies
     */
    private NotificationCenter notificationCenter;
    private NavigateServiceInterface navigation;
    private StorageManager storageManager;
    @InjectScope
    BoardScope scope;

    List<BoardModel> models;
    private ObservableList<BoardListItemViewModel> boardItems = FXCollections.observableArrayList();


    public void initialize() {
        // reset every time we load home page
        scope.setBoardModel(null);
        storageManager.setLazyMode(true);
        models = storageManager.getBoards();
        LOG.info("Loaded boards size: " + models.size());
        scope.setHasBoards(!models.isEmpty());
        LOG.info(scope.toString());
        boardItems.addAll(
                getModels().stream().map(BoardListItemViewModel::new)
                        .collect(Collectors.toList())
        );
    }

    public List<BoardModel> getModels() {
        return models;
    }

    public void setModels(List<BoardModel> models) {
        this.models = models;
    }

    public NotificationCenter getNotificationCenter() {
        return notificationCenter;
    }

    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public ObservableList<BoardListItemViewModel> getBoardItems() {
        return boardItems;
    }

    public void setBoardItems(ObservableList<BoardListItemViewModel> boardItems) {
        this.boardItems = boardItems;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }
    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public void edit(BoardListItemViewModel boardItemVM) throws ViewNotFoundException {
        scope.setBoardModel(boardItemVM.getModel());
        if (navigation != null) {
            navigation.navigate("board-form");
        }
    }

    public boolean remove(BoardListItemViewModel boardItemVM) throws ViewNotFoundException {
        BoardModel boardModel = boardItemVM.getModel();
        try {
            storageManager.removeBoard(boardModel);
        } catch (Exception e) {
            e.printStackTrace();
            notificationCenter.publish(Notification.INFO, "msg.generic_error");
            return false;
        }
        boardItems.remove(boardItemVM);
        notificationCenter.publish(Notification.INFO, "msg.board_deleted_success");
        if (boardItems.size() == 0) {
            scope.setHasBoards(false);
            navigation.navigate("start");
        }
        return true;
    }

    public void viewList(BoardListItemViewModel boardItemVM) throws ViewNotFoundException {
        scope.setBoardModel(boardItemVM.getModel());
        if (navigation != null) {
            navigation.navigate("board-items");
        }
    }

    public void addBoard() throws ViewNotFoundException {
        if (navigation != null) {
            navigation.navigate("board-form");
        }
    }
}
