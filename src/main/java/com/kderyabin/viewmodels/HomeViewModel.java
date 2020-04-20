package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.repository.BoardRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
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

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@ScopeProvider(scopes= BoardScope.class)
public class HomeViewModel implements ViewModel {
    final private Logger LOG = LoggerFactory.getLogger(HomeViewModel.class);
    /*
     *  Dependencies
     */
    private NotificationCenter notificationCenter;
    private BoardRepository repository;
    private NavigateServiceInterface navigation;
    @InjectScope
    BoardScope scope;

    List<BoardModel> models = new LinkedList<>();
    private ObservableList<BoardListItemViewModel> boardItems = FXCollections.observableArrayList();

    private void initModels() {
        repository.findAll().forEach(item -> models.add(item));
    }

    public void initialize() {
        // reset every time we get back to home
        scope.setBoardModel(null);
        initModels();
        LOG.info("Loaded boards size: " + models.size());
        scope.setHasBoards(!models.isEmpty());
        LOG.info(scope.toString());
        boardItems.addAll(
                getModels().stream().map(BoardListItemViewModel::new)
                        .collect(Collectors.toList())
        );
    }

    public BoardRepository getRepository() {
        return repository;
    }

    @Autowired
    public void setRepository(BoardRepository repository) {
        this.repository = repository;
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

    public void edit(BoardListItemViewModel boardItemVM) throws Exception {
        scope.setBoardModel(boardItemVM.getModel());
        if(navigation != null){
            navigation.navigate("board-form");
        }
    }

    public void remove(BoardListItemViewModel boardItemVM) {
        BoardModel boardModel = boardItemVM.getModel();
        repository.delete(boardModel);
        boardItems.remove(boardItemVM);
        notificationCenter.publish(Notification.INFO, "msg.board_deleted_success");
    }

    public void viewList(BoardListItemViewModel boardItemVM) throws Exception{
        scope.setBoardModel(boardItemVM.getModel());
        if(navigation != null){
            navigation.navigate("board-items");
        }
    }

    public void addBoard() throws Exception {
        if(navigation != null) {
            navigation.navigate("board-form");
        }
    }
}
