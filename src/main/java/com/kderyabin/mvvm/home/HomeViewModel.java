package com.kderyabin.mvvm.home;

import com.kderyabin.partagecore.model.BoardModel;
import com.kderyabin.services.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.RunService;
import com.kderyabin.partagecore.storage.StorageManager;
import com.kderyabin.util.Notification;
import com.kderyabin.mvvm.home.list.BoardListItemViewModel;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    private RunService runService;

    List<BoardModel> models;
//    private ObservableList<BoardListItemViewModel> boardItems = FXCollections.observableArrayList();
    private ListProperty<BoardListItemViewModel> boardItems = new SimpleListProperty<>(FXCollections.observableArrayList());


    public void initialize() {
        LOG.debug("Start initialize");
        // reset current board model in the scope every time we load the view.
        scope.setBoardModel(null);
        CompletableFuture.runAsync(this::initData, runService.getExecutorService());
        LOG.debug("End initialize");
    }

    /**
     * Loads data from DB
     */
    public void initData() {
        LOG.debug("Start initData");
        models = storageManager.getBoards();
        if (models.size() > 0) {
            boardItems.addAll(
                    models.stream()
                            .map(BoardListItemViewModel::new)
                            .collect(Collectors.toList()
                            )
            );
        }
        scope.setHasBoards(!models.isEmpty());
        LOG.debug("End initData");
    }

    public void edit(BoardListItemViewModel boardItemVM) {
        scope.setBoardModel(boardItemVM.getModel());
        if (navigation != null) {
            navigation.navigate("board-form");
        }
    }

    public boolean remove(BoardListItemViewModel boardItemVM) {
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

    public void viewList(BoardListItemViewModel boardItemVM) {
        scope.setBoardModel(boardItemVM.getModel());
        if (navigation != null) {
            navigation.navigate("board-items");
        }
    }

    public void addBoard() {
        if (navigation != null) {
            navigation.navigate("board-form");
        }
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
        return boardItems.get();
    }

    public ListProperty<BoardListItemViewModel> boardItemsProperty() {
        return boardItems;
    }

    public void setBoardItems(ObservableList<BoardListItemViewModel> boardItems) {
        this.boardItems.set(boardItems);
    }

    //    public ObservableList<BoardListItemViewModel> getBoardItems() {
//        return boardItems;
//    }
//
//    public void setBoardItems(ObservableList<BoardListItemViewModel> boardItems) {
//        this.boardItems = boardItems;
//    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public RunService getRunService() {
        return runService;
    }
    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }
}
