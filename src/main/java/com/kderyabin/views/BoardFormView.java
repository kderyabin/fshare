package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import org.springframework.stereotype.Component;

@Component
public class BoardFormView implements FxmlView<BoardViewModel> {

    @InjectViewModel
    private BoardViewModel viewModel;

    public void initialize() {

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        viewModel.goBack();
    }
}
