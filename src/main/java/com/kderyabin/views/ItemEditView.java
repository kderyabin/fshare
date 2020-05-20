package com.kderyabin.views;

import com.jfoenix.controls.JFXButton;
import com.kderyabin.controls.ConfirmAlert;
import com.kderyabin.viewmodels.ItemEditViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ItemEditView implements FxmlView<ItemEditViewModel> {
    @FXML
    public Label itemLabel;
    @FXML
    public TextField title;
    @FXML
    public TextField amount;
    @FXML
    public DatePicker date;
    @FXML
    public ComboBox<PersonListItemViewModel> participants;
    @FXML
    public JFXButton saveBtn;
    @FXML
    public JFXButton backBtn;

    @InjectViewModel
    private ItemEditViewModel viewModel;
    @InjectResourceBundle
    private ResourceBundle resources;

    public void initialize() {
        if(!viewModel.getTitle().isEmpty()) {
            itemLabel.textProperty().bind(viewModel.titleProperty());
        }
        title.textProperty().bindBidirectional(viewModel.titleProperty());
        amount.textProperty().bindBidirectional(viewModel.amountProperty());
        date.valueProperty().bindBidirectional(viewModel.dateProperty());
        participants.setItems(viewModel.getParticipants());
        participants.valueProperty().bindBidirectional(viewModel.personProperty());
        participants.setConverter( getComboConverter() );
    }

    public void goBack() throws Exception {

        if(!viewModel.canGoBack()){
            Alert alert = new ConfirmAlert(resources.getString("msg.confirm_form_exit"));
            Optional<ButtonType> option = alert.showAndWait();
            if( !option.isPresent() || option.get() != ButtonType.OK){
                return;
            }
        }
        viewModel.goBack();
    }

    public void save() {
        try {
            viewModel.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Converter to display a PersonListItemViewModel.
     * @return
     */
    private StringConverter<PersonListItemViewModel> getComboConverter(){
        return new StringConverter<PersonListItemViewModel>() {
            @Override
            public String toString(PersonListItemViewModel object) {
                return object.getName();
            }

            @Override
            public PersonListItemViewModel fromString(String string) {
                return participants.getItems().stream().filter( item ->
                        item.getName().equals(string)).findFirst().orElse(null);
            }
        };
    }
}
