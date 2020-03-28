package com.kderyabin.views;

import com.kderyabin.viewmodels.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public class MainView implements FxmlView<MainViewModel> {

    @FXML
    public Pane content;

    @InjectViewModel
    private MainViewModel viewModel;

    public void initialize() {


    }

    /**
     * Method used to navigate between pages resets components of the content area.
     * @param parent Panel with UI components.
     */
    public void setContent(Parent parent) {
        ObservableList<Node> children = content.getChildren();
        children.remove(0, children.size());
        children.add(parent);
    }
}
