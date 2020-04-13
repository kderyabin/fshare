package com.kderyabin.views;

import com.kderyabin.viewmodels.LinesListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LinesListItemView implements FxmlView<LinesListItemViewModel> {

    @InjectViewModel
    private LinesListItemViewModel viewModel;

    @FXML
    public Label title;
    @FXML
    public Label person;
    @FXML
    public Label amount;

    public void initialize() {
        title.textProperty().bind(viewModel.titleProperty());
        person.textProperty().bind(viewModel.personProperty());
        amount.textProperty().bind(viewModel.amountProperty());
    }
}
