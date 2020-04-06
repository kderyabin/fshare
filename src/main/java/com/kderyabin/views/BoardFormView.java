package com.kderyabin.views;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.kderyabin.Main;
import com.kderyabin.viewmodels.BoardViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BoardFormView implements FxmlView<BoardViewModel> {
    @FXML
    public BorderPane root;
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
    private BoardViewModel viewModel;


    public void initialize() {

        name.textProperty().bindBidirectional(viewModel.nameProperty());
        description.textProperty().bindBidirectional(viewModel.descriptionProperty());

        participantsList.setItems(viewModel.participantsProperty());
        participantsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(PersonListItemView.class));
        participantsList.addEventHandler(ActionEvent.ACTION, this::removeParticipant);
    }

    public void goBack() {

        if(!viewModel.goBack()){
//            JFXDialog dialog = new JFXDialog();
//            dialog.setContent(new Label("Content"));
//            button.setOnAction((action)->dialog.show(rootStackPane));

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete File");
            //alert.setHeaderText("Are you sure want to move this file to the Recycle Bin?");
            alert.setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit,"
                    + " sed do eiusmod tempor incididunt ut labore et dolore magna"
                    + " aliqua. Utenim ad minim veniam, quis nostrud exercitation"
                    + " ullamco laboris nisi ut aliquip ex ea commodo consequat.");
            Optional<ButtonType> option = alert.showAndWait();

        }
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
