package com.kderyabin.views;

import com.kderyabin.Main;
import com.kderyabin.viewmodels.StartViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;

public class StartView implements FxmlView<StartViewModel> {

    @InjectViewModel
    private StartViewModel viewModel;

    public void initialize() {

    }

    @FXML
    public void btnClick() throws Exception {
        Main.setContent("boardForm");
    }
}
