package com.kderyabin.views;

import com.jfoenix.controls.JFXButton;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PersonListItemView implements FxmlView<PersonListItemViewModel> {

    @FXML
    public Label name;
    @FXML
    public JFXButton removeButton;

    @InjectViewModel
    private PersonListItemViewModel viewModel;

    public void initialize() {
        name.textProperty().bind(viewModel.nameProperty());
        removeButton.setUserData(viewModel);
    }
}
