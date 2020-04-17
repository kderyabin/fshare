package com.kderyabin.services;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class NavigateService implements NavigateServiceInterface {

    //private static NavigateService instance;

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

    /**
     * Add view class to the map of navigable classes this making it available for navigation.
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
    public Parent loadContent(String name) throws Exception {
        if (!list.containsKey(name)) {
            throw new Exception("View is undefined for the name: " + name);
        }
        return FluentViewLoader.fxmlView(list.get(name)).load().getView();
    }

    /**
     * Method used to navigate between pages resets components of the content area.
     *
     * @param viewName See loadContent.
     * @throws Exception
     */
    @Override
    public void navigate(String viewName) throws Exception {
        final Parent parent = loadContent(viewName);
        previous = viewName;
        ObservableList<Node> children = content.getChildren();
        children.clear();
        children.add(parent);
    }

    public Pane getContent() {
        return content;
    }

    public void setContent(Pane content) {
        this.content = content;
    }
}

