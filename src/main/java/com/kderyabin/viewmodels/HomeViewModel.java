package com.kderyabin.viewmodels;

import com.kderyabin.dao.BoardRepository;
import com.kderyabin.models.BoardModel;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class HomeViewModel implements ViewModel {
    /*
     *  Dependencies
     */
    private NotificationCenter notificationCenter;
    private BoardRepository repository;
    private NavigateServiceInterface navigation;

    List<BoardModel> models = new LinkedList<>();
    private ObservableList<BoardListItemViewModel> boardItems = FXCollections.observableArrayList();

    private void initModels() {
        repository.findAll().forEach(item -> models.add(item));
    }

    public void initialize() {
        initModels();
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
}
