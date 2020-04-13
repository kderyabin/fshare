package com.kderyabin.views;

import com.jfoenix.controls.JFXListView;
import com.kderyabin.viewmodels.ItemEditViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ItemEditView implements FxmlView<ItemEditViewModel> {


    @InjectViewModel
    private ItemEditViewModel viewModel;
    @FXML
    public TextField title;
    @FXML
    public TextField amount;
    @FXML
    public DatePicker date;
    @FXML
    public ListView person;
    
    
    public void initialize() {

    }

    public void goBack(ActionEvent actionEvent) {
        if (viewModel.canGoBack()) {
            viewModel.goBack();

        }
    }

    public void save(ActionEvent actionEvent) {
        viewModel.save();
    }
}
