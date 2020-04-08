package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BoardListItemView implements FxmlView<BoardListItemViewModel> {
    @InjectViewModel
    private BoardListItemViewModel viewModel;
    @FXML
    public Label name;
    @FXML
    public Button editBtn;
    @FXML
    public Button removeBtn;

    public void initialize() {
        name.textProperty().bind(viewModel.nameProperty());
        editBtn.setUserData(viewModel);
        removeBtn.setUserData(viewModel);
    }

}
