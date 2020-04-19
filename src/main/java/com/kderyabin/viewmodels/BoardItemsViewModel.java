package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.repository.BoardItemRepository;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ScopeProvider;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
@ScopeProvider(scopes = BoardScope.class)
public class BoardItemsViewModel implements ViewModel {

    final private static Logger LOG = LoggerFactory.getLogger(BoardItemsViewModel.class);
    /*
     * Dependencies
     */
    private NavigateServiceInterface navigation;
    private BoardItemRepository itemRepository;
    private BoardModel model;
    private BoardScope scope;

    private StringProperty boardName  = new SimpleStringProperty("");
    private ObservableList<LinesListItemViewModel> lines = FXCollections.observableArrayList();

    public void initialize() {
        model = scope.getBoardModel();
        scope.setItemModel(null);
        List<BoardItemModel> items = (ArrayList<BoardItemModel>) itemRepository.findAllByBoardId(model.getId());
        LOG.debug("Board lines found:" + items.size());
        setBoardName(model.getName());
        if(items.size() > 0) {
            lines.addAll(
                    items.stream().map(LinesListItemViewModel::new).collect(Collectors.toList())
            );

        }
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public BoardItemRepository getItemRepository() {
        return itemRepository;
    }

    @Autowired
    public void setItemRepository(BoardItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public BoardModel getModel() {
        return model;
    }

    public void setModel(BoardModel model) {
        this.model = model;
    }

    public String getBoardName() {
        return boardName.get();
    }

    public StringProperty boardNameProperty() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName.set(boardName);
    }

    public ObservableList<LinesListItemViewModel> getLines() {
        return lines;
    }

    public void setLines(ObservableList<LinesListItemViewModel> lines) {
        this.lines = lines;
    }

    public BoardScope getScope() {
        return scope;
    }

    @Autowired
    public void setScope(BoardScope scope) {
        this.scope = scope;
    }

    public void editItem(LinesListItemViewModel linesListItemViewModel) throws Exception{
        scope.setItemModel(linesListItemViewModel.getModel());
        navigation.navigate("board-item");
    }

    public void addItem() throws Exception{
        navigation.navigate("board-item");
    }

    public void goBack() throws Exception {
        navigation.navigate("home");
    }
}
