package com.kderyabin.services;

import com.kderyabin.error.ViewNotFoundException;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class NavigateService implements NavigateServiceInterface {

    final private Logger LOG = LoggerFactory.getLogger(NavigateService.class);
    /**
     * list of views.
     */
    Map<String, Class<? extends FxmlView<? extends ViewModel>>> list = new HashMap<>();

    /**
     * Content display area.
     */
    private Pane content;

    /**
     * Previous view's name.
     */
    private String previous;

    private ViewTuple current;

    /**
     * Add view class to the map of navigable classes thus making it available for navigation.
     *
     * @param name  Some name to associate with the View.
     * @param clazz View class, something like MainView.class.
     */
    public void register(String name, Class<? extends FxmlView<? extends ViewModel>> clazz) {
        list.put(name, clazz);
    }

    /**
     * Loads UI components.
     *
     * @param name Name for the View class, something like "main".
     */
    public Parent loadContent(String name) throws ViewNotFoundException {
        if (!list.containsKey(name)) {
            return null;
//            throw new ViewNotFoundException("View is undefined for the name: " + name);
        }
        current = FluentViewLoader.fxmlView(list.get(name)).load();
        return current.getView();
    }

    /**
     * Method used to navigate between pages.
     * It loads a new view and resets components of the content area.
     *
     * @param viewName See loadContent.
     * @throws  ViewNotFoundException
     */
    @Override
    public void navigate(String viewName) throws ViewNotFoundException {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final Parent parent = loadContent(viewName);
                Platform.runLater(() -> {
                    ObservableList<Node> children = content.getChildren();
                    children.clear();
                    children.add(parent);
                });
                return null;
            }
        };
        new Thread(task).start();
    }

    public Pane getContent() {
        return content;
    }

    public void setContent(Pane content) {
        this.content = content;
    }

    /**
     * Get current viewModel if there is one.
     * @return
     */
    public ViewModel getCurrentViewModel(){
        if(current != null) {
            return current.getViewModel();
        }
        return null;
    }
}

