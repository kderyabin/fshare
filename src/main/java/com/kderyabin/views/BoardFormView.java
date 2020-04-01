package com.kderyabin.views;

import com.kderyabin.viewmodels.BoardViewModel;
import com.kderyabin.viewmodels.PersonListItemViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class BoardFormView implements FxmlView<BoardViewModel> {
    @FXML
    public TextField name;
    @FXML
    public TextArea description;
    @FXML
    public ListView<PersonListItemViewModel> participantsList;

    @InjectViewModel
    private BoardViewModel viewModel;


    public void initialize() {
        name.textProperty().bindBidirectional(viewModel.nameProperty());
        description.textProperty().bindBidirectional(viewModel.descriptionProperty());
        participantsList.setItems(viewModel.participantsProperty());
        participantsList.setCellFactory(CachedViewModelCellFactory.createForFxmlView(PersonListItemView.class));

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        viewModel.goBack();
    }
}
