package com.kderyabin.views;

import com.kderyabin.Main;
import com.kderyabin.viewmodels.BoardViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;

public class BoardFormView implements FxmlView<BoardViewModel> {

    @InjectViewModel
    private BoardViewModel viewModel;

    public void initialize() {

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        Main.setContent("main");
    }
}
