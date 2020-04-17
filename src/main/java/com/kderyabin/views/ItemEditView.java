package com.kderyabin.views;

import com.kderyabin.viewmodels.ItemEditViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
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
    public ComboBox<PersonListItemViewModel> participants;

    public void initialize() {
        title.textProperty().bindBidirectional(viewModel.titleProperty());
        amount.textProperty().bindBidirectional(viewModel.amountProperty());
        date.valueProperty().bindBidirectional(viewModel.dateProperty());
        participants.setItems(viewModel.getParticipants());
        participants.valueProperty().bindBidirectional(viewModel.personProperty());
    }

    public void goBack(ActionEvent actionEvent) {
        try {
            viewModel.goBack();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void save() {
        viewModel.save();
    }
}
