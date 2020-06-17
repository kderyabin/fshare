package com.kderyabin.mvvm.board.details;

import com.kderyabin.model.BoardItemModel;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.services.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.RunService;
import com.kderyabin.services.StorageManager;
import com.kderyabin.mvvm.board.details.list.LinesListItemViewModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class BoardItemsViewModel implements ViewModel {

    final private static Logger LOG = LoggerFactory.getLogger(BoardItemsViewModel.class);
    /*
     * Dependencies
     */
    private RunService runService;
    private NavigateServiceInterface navigation;
    private BoardModel model;
    private BoardScope scope;
    private StorageManager storageManager;

    private StringProperty boardName = new SimpleStringProperty("");
    /**
     * Items (lines) of the board.
     */
    private ListProperty<LinesListItemViewModel> lines = new SimpleListProperty<>(FXCollections.observableArrayList());

    private BooleanProperty boardEmpty = new SimpleBooleanProperty(false);
    /**
     * Status of items loading.
     * Set to true when async loading is done.
     */
    private BooleanProperty linesLoaded = new SimpleBooleanProperty(false);
    /**
     * Expenses of every board participant to display in a chart.
     */
    private Map<String, Double> chartData = new HashMap<>();
    /**
     * Status of data chart loading.
     * Set to true when async loading is done.
     */
    private BooleanProperty chartLoaded = new SimpleBooleanProperty(false);

    public void initialize() {
        LOG.debug("Start Initialize");
        model = scope.getBoardModel();
        // Load board items
        CompletableFuture
                .supplyAsync(() -> initLines(model), runService.getExecutorService())
                .thenAccept(items -> model.setItems(items))
                .thenRun(() -> setLinesLoaded(true));
        /// Initialize chart data
        CompletableFuture
                .runAsync(() -> initDataChart(model), runService.getExecutorService())
                .thenRun(() -> setChartLoaded(true));
        if(model.getParticipants().size() == 0) {
            CompletableFuture
                    .runAsync(() -> initParticipants(model), runService.getExecutorService());
        }

        scope.setItemModel(null);
        // Load board participants
        LOG.debug("End Initialize");
    }

    private void initParticipants(BoardModel model){
        LOG.debug("Start initParticipants");
        storageManager.loadParticipants(model);
        LOG.debug("End initParticipants");
    }
    private List<BoardItemModel> initLines(BoardModel model){
        LOG.debug("Init board lines");
        List<BoardItemModel> itemModels = storageManager.getItems(model);
        if (itemModels.size() > 0) {
            lines.addAll(
                    itemModels.stream()
                            .map(LinesListItemViewModel::new)
                            .collect(Collectors.toList())
            );

        }
        setBoardEmpty(itemModels.isEmpty());
        LOG.debug("End Init board lines");
        return itemModels;
    }
    /**
     * Prepare labels for the chart.
     *
     * @param item BoardPersonTotal instance.
     * @return Ready for display string.
     */
    private String getChartItemLabel(BoardPersonTotal item) {
        return String.format(
                "%s (%s %s)",
                item.getPerson().getName(),
                item.getTotal().doubleValue(),
                model.getCurrencyCode()
        );
    }

    /**
     * Loads chart data from DB and prepares it for the PieChart.Data.
     * @param model BoardModel instance.
     */
    public void initDataChart(BoardModel model) {
        LOG.debug("Init chart data");
        List<BoardPersonTotal> list =  storageManager.getBoardPersonTotal(model.getId());
        list.forEach(item -> {
            String name = getChartItemLabel(item);
            Double amount = item.getTotal().doubleValue();
            chartData.put(name, amount);
        });
        LOG.debug("End Init chart data");
    }

    public void editItem(LinesListItemViewModel linesListItemViewModel) {
        scope.setItemModel(linesListItemViewModel.getModel());
        if(navigation != null) {
            navigation.navigate("board-item");
        }
    }

    public void addItem() {
        if( navigation != null) {
            navigation.navigate("board-item");
        }
    }

    public void goBack() {
        if( navigation != null) {
            navigation.navigate("home");
        }
    }

    public void goToBalance() {
        if( navigation != null) {
            navigation.navigate("balance");
        }
    }

    /**
     * Getters / Setters
    */
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
        return lines.get();
    }

    public ListProperty<LinesListItemViewModel> linesProperty() {
        return lines;
    }

    public void setLines(ObservableList<LinesListItemViewModel> lines) {
        this.lines.set(lines);
    }

    public Map<String, Double> getChartData() {
        return chartData;
    }

    public void setChartData(Map<String, Double> chartData) {
        this.chartData = chartData;
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

    public RunService getRunService() {
        return runService;
    }

    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public boolean getChartLoaded() {
        return chartLoaded.get();
    }

    public BooleanProperty chartLoadedProperty() {
        return chartLoaded;
    }

    public void setChartLoaded(boolean chartLoaded) {
        this.chartLoaded.set(chartLoaded);
    }

    public boolean isLinesLoaded() {
        return linesLoaded.get();
    }

    public BooleanProperty linesLoadedProperty() {
        return linesLoaded;
    }

    public void setLinesLoaded(boolean linesLoaded) {
        this.linesLoaded.set(linesLoaded);
    }

    public boolean isBoardEmpty() {
        return boardEmpty.get();
    }

    public BooleanProperty boardEmptyProperty() {
        return boardEmpty;
    }

    public void setBoardEmpty(boolean boardEmpty) {
        this.boardEmpty.set(boardEmpty);
    }
}
