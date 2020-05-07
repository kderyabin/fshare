package com.kderyabin.viewmodels;

import com.kderyabin.error.ViewNotFoundException;
import com.kderyabin.model.BoardModel;
import com.kderyabin.scopes.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.StorageManager;
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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class BoardItemsViewModel implements ViewModel {

    final private static Logger LOG = LoggerFactory.getLogger(BoardItemsViewModel.class);
    /*
     * Dependencies
     */
    private NavigateServiceInterface navigation;
    private BoardModel model;
    private BoardScope scope;
    private StorageManager storageManager;

    private StringProperty boardName  = new SimpleStringProperty("");
    private ObservableList<LinesListItemViewModel> lines = FXCollections.observableArrayList();
    private Map<String, Double> chartData = new HashMap<>();

    public void initialize() {
        LOG.info("Initialize");
        scope.setItemModel(null);
        model = scope.getBoardModel();
        LOG.info("Model in scope with ID:" + model.getId());
        // refresh participants list
        model = storageManager.loadParticipants(model);
        LOG.info("Participants size:" + model.getParticipants().size());
        model = storageManager.loadItems(model);
        LOG.info("Board lines found:" + model.getItems().size());
        // Update model in the scope with the one with lines
        scope.setBoardModel(model);
        setBoardName(model.getName());
        if(model.getItems().size() > 0) {
            lines.addAll(
                    model.getItems()
                            .stream()
                            .map( item -> {
                                String chartKey = item.getPerson().getName();
                                Double amount = item.getAmount().doubleValue();
                                if(!chartData.containsKey(chartKey)) {
                                    chartData.put(chartKey, amount);
                                } else {
                                    Double sum = chartData.get(chartKey);
                                    sum += amount;
                                    chartData.put(chartKey, sum);
                                }
                                return new LinesListItemViewModel(item);
                            })
                            .collect(Collectors.toList())
            );
        }
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
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

    public StorageManager getStorageManager() {
        return storageManager;
    }
    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public Map<String, Double> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, Double> chartData) {
        this.chartData = chartData;
    }

    public void editItem(LinesListItemViewModel linesListItemViewModel) throws ViewNotFoundException {
        scope.setItemModel(linesListItemViewModel.getModel());
        navigation.navigate("board-item");
    }

    public void addItem() throws ViewNotFoundException{
        navigation.navigate("board-item");
    }

    public void goBack() throws ViewNotFoundException {
        navigation.navigate("home");
    }
}
