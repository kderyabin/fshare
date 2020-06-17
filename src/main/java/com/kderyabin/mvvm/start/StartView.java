package com.kderyabin.mvvm.start;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

@Component
public class StartView implements FxmlView<StartViewModel> {

    @InjectViewModel
    private StartViewModel viewModel;

    public void initialize() {

    }

    @FXML
    public void addBoard(){
        viewModel.editBoard();
    }
}
