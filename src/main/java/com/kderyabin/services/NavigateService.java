package com.kderyabin.services;

import com.kderyabin.views.*;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Default implementation of {@link NavigateServiceInterface} which allows navigation
 * by loading asynchronously requested view into the content display area.
 */
@Service
public class NavigateService implements NavigateServiceInterface {

    final private Logger LOG = LoggerFactory.getLogger(NavigateService.class);
    /**
     * list of views.
     */
    Map<String, Class<? extends FxmlView<? extends ViewModel>>> list = new HashMap<>();

    /**
     * Menu display area.
     */
    private Pane menuContainer;

    /**
     * Content display area.
     */
    private Pane content;

    /**
     * Currently displayed ViewTuple.
     */
    private ViewTuple<? extends FxmlView<? extends ViewModel>,? extends ViewModel> current;

    /**
     * Thread pool managing service.
     */
    private RunService runService;

    /**
     * Constructor.
     * Registers default navigation.
     */
    public NavigateService() {
        registerNavigation();
    }

    /**
     * @see NavigateServiceInterface for description.
     */
    @Override
    public void register(String name, Class<? extends FxmlView<? extends ViewModel>> clazz) {
        list.put(name, clazz);
    }

    /**
     * Initializes views for navigation.
     */
    private void registerNavigation() {
        register("home", HomeView.class);
        register("start", StartView.class);
        register("board-form", BoardFormView.class);
        register("board-items", BoardItemsView.class);
        register("board-item", ItemEditView.class);
        register("balance", BoardBalanceView.class);
        register("settings", SettingsView.class);
        register("menu", MenuView.class);
    }

    /**
     * Loads UI components.
     * The view name must registered with NavigateService.register() method.
     *
     * @param name Name for the View class, something like "main", "home", et...
     * @return View to display
     */
    public Parent loadContent(String name) {
        if (!list.containsKey(name)) {
            LOG.info("View is undefined for the name: " + name);
            return null;
        }
        current = FluentViewLoader.fxmlView(list.get(name)).load();
        return current.getView();
    }

    /**
     * @see NavigateServiceInterface#navigate(String)
     */
    @Override
    public void navigate(String viewName) {
        CompletableFuture.runAsync(() -> {
            final Parent parent = loadContent(viewName);
            Platform.runLater(() -> {
                ObservableList<Node> children = content.getChildren();
                children.clear();
                children.add(parent);
            });
        }, runService.getExecutorService());
    }

    /**
     * @see NavigateServiceInterface#getContent()
     */
    @Override
    public Pane getContent() {
        return content;
    }

    /**
     *
     * @see NavigateServiceInterface#setContent(Pane)
     */
    @Override
    public void setContent(Pane content) {
        this.content = content;
    }

    @Override
    public void setMenuContainer(Pane menuContainer) {
        this.menuContainer = menuContainer;
    }

    /**
     * @see NavigateServiceInterface#reloadMenu()
     */
    @Override
    public void reloadMenu() {
        if (!list.containsKey("menu")) {
            LOG.info("View is undefined for the name: menu");
            return;
        }
        CompletableFuture.runAsync(() -> {
            Parent menuContent =  FluentViewLoader.fxmlView(list.get("menu")).load().getView();
            Platform.runLater(() -> {
                ObservableList<Node> children = menuContainer.getChildren();
                children.clear();
                children.add(menuContent);
            });
        }, runService.getExecutorService());
    }

    /**
     * @see NavigateServiceInterface#getCurrentViewModel()
     */
    @Override
    public ViewModel getCurrentViewModel() {
        if (current != null) {
            return current.getViewModel();
        }
        return null;
    }

    /**
     * Getter.
     * @see RunService
     * @return  RunService
     */
    public RunService getRunService() {
        return runService;
    }

    /**
     * Setter.
     * @see RunService
     * @param runService Instance of RunService
     */
    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }
}

