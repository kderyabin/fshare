package com.kderyabin.services;

import com.kderyabin.error.ViewNotFoundException;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.layout.Pane;

public interface NavigateServiceInterface {
    /**
     * Add view class to the map of navigable classes this making it available for navigation.
     *
     * @param name  Some name to associate with the View.
     * @param clazz View class name, something like MainView.class.
     */
    void register(String name, Class<? extends FxmlView<? extends ViewModel>> clazz);
    void navigate(String viewName) throws ViewNotFoundException;
    Pane getContent();
    void setContent(Pane content);
    ViewModel getCurrentViewModel();
}
