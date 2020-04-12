package com.kderyabin.views;

import com.kderyabin.viewmodels.MenuViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import org.springframework.stereotype.Component;

@Component
public class MenuView implements FxmlView<MenuViewModel> {

    @InjectViewModel
    private MenuViewModel viewModel;


    public void createNewBoard(ActionEvent actionEvent) throws Exception {
        viewModel.createNewBoard();
    }

    public void exit(ActionEvent actionEvent) {
        viewModel.exit();
    }
}
