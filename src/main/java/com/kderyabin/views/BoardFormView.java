package com.kderyabin.views;

import com.kderyabin.models.BoardModel;
import com.kderyabin.viewmodels.BoardViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class BoardFormView implements FxmlView<BoardViewModel> {
    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public ListView<PersonListItemViewModel> participantsList;
    @FXML
    public TextField person;

    @InjectViewModel
    private BoardViewModel viewModel;


    public void initialize() {
        name.textProperty().bindBidirectional(viewModel.nameProperty());
        description.textProperty().bindBidirectional(viewModel.descriptionProperty());

        participantsList.setItems(viewModel.participantsProperty());
        participantsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(PersonListItemView.class));
        participantsList.addEventHandler(ActionEvent.ACTION, this::removeParticipant);
    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        viewModel.goBack();
    }

    /**
     * Event handler adds participant to the board.
     */
    public void addParticipant() {
        if( viewModel.addParticipant(person.getText())){
            person.setText("");
        }
    }

    /**
     * Event handler removes a participant from the board.
     * @param event
     */
    public void removeParticipant(ActionEvent event) {
        if(event.getTarget() instanceof Button){
            Button btn = (Button) event.getTarget();
            PersonListItemViewModel vm = (PersonListItemViewModel) btn.getUserData();
            viewModel.removeParticipant(vm);
        }
    }
}
