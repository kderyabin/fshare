package com.kderyabin.views;

import com.kderyabin.viewmodels.MenuViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MenuView implements FxmlView<MenuViewModel> {

    @FXML
    public MenuItem exitAppButton;

    @InjectViewModel
    private MenuViewModel viewModel;

    public void initialize() {
        exitAppButton.setOnAction( e -> viewModel.exit());
    }

}
