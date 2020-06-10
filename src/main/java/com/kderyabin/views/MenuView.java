package com.kderyabin.views;

import com.kderyabin.controls.ConfirmAlert;
import com.kderyabin.viewmodels.MenuViewModel;
import com.sun.javafx.stage.StageHelper;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class MenuView implements FxmlView<MenuViewModel> {
    @InjectViewModel
    private MenuViewModel viewModel;
    @InjectResourceBundle
    private ResourceBundle resources;

    /**
     * Event handler for the new board creation.
     */
    public void createNewBoard() {
        if (!viewModel.canUnloadCurrentView()) {
            Alert alert = new ConfirmAlert(resources.getString("msg.confirm_form_exit"));
            Optional<ButtonType> option = alert.showAndWait();
            if (!option.isPresent() || option.get() != ButtonType.OK) {
                return;
            }
        }
        viewModel.createNewBoard();
    }

    /**
     * Event handler for Quit item menu.
     */
    public void exit() {
        viewModel.exit();
    }

    /**
     * Event handler for save action.
     */
    public void save() {
        viewModel.save();
    }

    public void showSettings(){
        viewModel.showSettings();
    }
}
