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
     * Previous view's name.
     */
    private String previous;
    /**
     * Currently displayed ViewTuple.
     */
    private ViewTuple current;

    private RunService runService;

    /**
     * Add view class to the map of navigable classes thus making it available for
     *
     * @param name  Some name to associate with the View.
     * @param clazz View class, something like MainView.class.
     */
    @Override
    public void register(String name, Class<? extends FxmlView<? extends ViewModel>> clazz) {
        list.put(name, clazz);
    }

    public NavigateService() {
        registerNavigation();
    }


    /**
     * Initialize views for
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
     * The view name must registered with this.registered() method.
     *
     * @param name Name for the View class, something like "main".
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
     * Method used to navigate between pages.
     * It loads a new view and resets components of the content area.
     *
     * @param viewName See loadContent.
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

    @Override
    public Pane getContent() {
        return content;
    }

    @Override
    public void setContent(Pane content) {
        this.content = content;
    }

    @Override
    public void setMenuContainer(Pane menuContainer) {
        this.menuContainer = menuContainer;
    }

    /**
     * Reloads menu
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
     * Get current viewModel if there is one.
     *
     * @return
     */
    @Override
    public ViewModel getCurrentViewModel() {
        if (current != null) {
            return current.getViewModel();
        }
        return null;
    }

    public RunService getRunService() {
        return runService;
    }

    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }
}

