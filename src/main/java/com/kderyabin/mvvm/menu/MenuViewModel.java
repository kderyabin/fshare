package com.kderyabin.mvvm.menu;

import com.kderyabin.mvvm.EditableInterface;
import com.kderyabin.services.BoardScope;
import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuViewModel implements ViewModel {

    final private Logger LOG = LoggerFactory.getLogger(MenuViewModel.class);

    private NavigateServiceInterface navigation;
    /**
     * Current scope to manage currently selected in application the board.
     */
    private BoardScope scope;

    public void exit() {
        Platform.exit();
        System.exit(0);
    }

    public void createNewBoard() {
        // reset currently selected board
        // otherwise the board form will be populated with its data.
        scope.resetSelection();
        if (navigation != null) {
            navigation.navigate("board-form");
        }
    }

    /**
     * Checks whether current viewModel implements EditableInterface
     *
     * @return Instance of EditableInterface.
     */
    private EditableInterface getCurrentEditable() {
        ViewModel current = navigation.getCurrentViewModel();
        if (current != null && EditableInterface.class.isAssignableFrom(current.getClass())) {
            return (EditableInterface) current;
        }
        return null;
    }

    /**
     * Checks if currently displayed view can be safely replaced with another view.
     *
     * @return TRUE if currently displayed view can be replaced FALSE otherwise.
     */
    public boolean canUnloadCurrentView() {
        EditableInterface editable = getCurrentEditable();
        if (editable != null) {
            return editable.isUpdated();
        }
        return true;
    }

    // Getters / Setters

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public BoardScope getScope() {
        return scope;
    }

    @Autowired
    public void setScope(BoardScope scope) {
        this.scope = scope;
    }

    /**
     * Triggers save action in currently displayed viewModel.
     */
    public void save() {
        EditableInterface editable = getCurrentEditable();
        if (editable != null) {
            editable.save();
        }
    }

    public void showSettings() {

        if(navigation != null) {
            navigation.navigate("settings");
        }
    }
}
