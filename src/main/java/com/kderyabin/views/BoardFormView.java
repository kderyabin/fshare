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
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class BoardFormView implements FxmlView<BoardFormViewModel> {

    final private static Logger LOG = LoggerFactory.getLogger(BoardFormView.class);
    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public ListView<PersonListItemViewModel> participantsList;
    @FXML
    public TextField person;
    @FXML
    public Label noParticipantsLabel;
    @FXML
    public ComboBox<PersonListItemViewModel> personsChoice;
    @FXML
    public VBox choiceLabelContainer;
    @FXML
    public VBox choiceListContainer;

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
        initParticipantsListDisplay();
        if(false == viewModel.getPersons().size() > 0) {
            personsChoice.setItems(viewModel.getPersons());
            personsChoice.setConverter(getPersonsComboConverter());
        } else {
            choiceLabelContainer.setVisible(false);
            choiceLabelContainer.setManaged(false);
            choiceListContainer.setVisible(false);
            choiceListContainer.setManaged(false);
        }
    }

    /**
     * Toggles display of participants list and the participants label.
     */
    private void initParticipantsListDisplay(){
        final int count =  participantsList.getItems().size();
        if (count == 0) {
            participantsList.setManaged(false);
            participantsList.setVisible(false);
            noParticipantsLabel.setManaged(true);
            noParticipantsLabel.setVisible(true);
        } else if( count == 1){
            participantsList.setManaged(true);
            participantsList.setVisible(true);
            noParticipantsLabel.setManaged(false);
            noParticipantsLabel.setVisible(false);
        }
    }

    public void goBack() throws Exception {
        if (!viewModel.canGoBack()) {
            Alert alert = new ConfirmAlert(resources.getString("msg.confirm_form_exit"));
            Optional<ButtonType> option = alert.showAndWait();
            if (!option.isPresent() || option.get() != ButtonType.OK) {
                return;
            }
        }
        viewModel.goBack();
    }

    /**
     * Event handler adds participant to the board.
     */
    public void addParticipant() {
        if(!personsChoice.getSelectionModel().isEmpty()) {
            if(viewModel.addParticipant(personsChoice.getSelectionModel().getSelectedItem())) {
               // reset the combobox
               personsChoice.getSelectionModel().clearSelection();
            }
        } else {
            if (viewModel.addParticipant(person.getText())) {
                // reset the field content
                person.setText("");
            }
        }
        initParticipantsListDisplay();
        participantsList.layout();

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
            initParticipantsListDisplay();
            participantsList.layout();
        }
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
    private StringConverter<PersonListItemViewModel> getPersonsComboConverter(){
        return new StringConverter<PersonListItemViewModel>() {
            @Override
            public String toString(PersonListItemViewModel object) {
                return object.getName();
            }

            @Override
            public PersonListItemViewModel fromString(String string) {
                return personsChoice
                        .getItems()
                        .stream()
                        .filter( item -> item.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        };
    }
}
