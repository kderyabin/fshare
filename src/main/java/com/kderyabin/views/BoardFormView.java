package com.kderyabin.views;

import com.kderyabin.controls.ConfirmAlert;
import com.kderyabin.viewmodels.BoardFormViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class BoardFormView implements FxmlView<BoardFormViewModel> {

    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public ListView<PersonListItemViewModel> participantsList;
    @FXML
    public TextField person;
    @FXML
    public Button saveBtn;

    @InjectViewModel
    private BoardFormViewModel viewModel;
    @InjectResourceBundle
    private ResourceBundle resources;

    public void initialize() {

        name.textProperty().bindBidirectional(viewModel.nameProperty());
        description.textProperty().bindBidirectional(viewModel.descriptionProperty());

        participantsList.setItems(viewModel.participantsProperty());
        participantsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(PersonListItemView.class));
        participantsList.addEventHandler(ActionEvent.ACTION, this::removeParticipant);
    }

    public void goBack() throws Exception {
        if(!viewModel.canGoBack()){
            Alert alert = new ConfirmAlert("msg.confirm_form_exit");
            Optional<ButtonType> option = alert.showAndWait();
            if( !option.isPresent() || option.get() != ButtonType.OK){
                return;
            }
        }
        viewModel.goBack();
    }

    /**
     * Event handler adds participant to the board.
     */
    public void addParticipant() {

        if (viewModel.addParticipant(person.getText())) {
            // reset the field content
            person.setText("");
        }
    }

    /**
     * Event handler removes a participant from the board.
     *
     * @param event
     */
    public void removeParticipant(ActionEvent event) {
        if (event.getTarget() instanceof Button) {
            Button btn = (Button) event.getTarget();
            PersonListItemViewModel vm = (PersonListItemViewModel) btn.getUserData();
            viewModel.removeParticipant(vm);
        }
    }

    public void save() {
        try {
            viewModel.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
