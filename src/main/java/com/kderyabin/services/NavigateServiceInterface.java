package com.kderyabin.services;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.layout.Pane;
/**
 * Application navigation service allows navigation between different "screens"
 * by loading asynchronously requested view into the content display area.
 * A term screen is used because the application does not provide navigation between scenes or stages.
 * It simply loads requested view into the display area.
 * NavigateService allows also to reload a menu bar which actually goes beyond the purpose of this class.
 * It's done to keep the navigation logic in one place.
 * Important note, to load some view you must first register it for navigation using NavigationService.register() method.
 */
public interface NavigateServiceInterface {
    /**
     * Registers view classes for the navigation.
     *
     * @param name  Some name to associate with the View.
     * @param clazz View class, something like MainView.class.
     */
     void register(String name, Class<? extends FxmlView<? extends ViewModel>> clazz);

    /**
     * Method used to navigate between pages.
     * It loads a new view and resets components of the content area.
     *
     * @param viewName See loadContent.
     */
    void navigate(String viewName);

    /**
     * Returns main container Node.
     * @return Instance of Pane class
     */
    Pane getContent();

    /**
     * Set main container Node (content area) in which views are loaded.
     *
     * @param content layout pane container of the display area.
     */
    void setContent(Pane content);

    /**
     * Returns a ViewModel instance associated with current View.
     *
     * @return ViewModel or NULL
     */
    ViewModel getCurrentViewModel();

    /**
     * Sets a menu bar container which is required for reloading a menu bar.
     * @param menuContainer MenuBar container
     */
    void setMenuContainer(Pane menuContainer);
     /**
     * Reloads menu bar.
     */
    void reloadMenu();
}
