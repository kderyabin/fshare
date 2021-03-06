package com.kderyabin.mvvm.board.details.list;

import com.kderyabin.mvvm.board.details.list.LinesListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class LinesListItemView implements FxmlView<LinesListItemViewModel> {

    @InjectViewModel
    private LinesListItemViewModel viewModel;

    @FXML
    public Label title;
    @FXML
    public Label person;
    @FXML
    public Label amount;
    @FXML
    public Label currency;
    @FXML
    public Label date;

    public void initialize() {
        title.textProperty().bind(viewModel.titleProperty());
        amount.textProperty().bind(viewModel.amountProperty());
        person.textProperty().bind(viewModel.personProperty());
        currency.textProperty().bind(viewModel.currencyProperty());
        date.textProperty().bind(viewModel.dateProperty());
    }
}
