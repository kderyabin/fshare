package com.kderyabin.views;

import com.kderyabin.viewmodels.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.springframework.stereotype.Component;

@Component
public class MainView implements FxmlView<MainViewModel> {

    @FXML
    public Pane content;

    @InjectViewModel
    private MainViewModel viewModel;

    public void initialize() {


    }

    public Pane getContent() {
        return content;
    }

    //    /**
//     * Method used to navigate between pages resets components of the content area.
//     * @param parent Panel with UI components.
//     */
    public void goTo(Parent parent) {
        ObservableList<Node> children = content.getChildren();
        System.out.println(children.size());
        System.out.println(parent);
        children.clear();
        System.out.println(children.size());
        if(parent != null) {
            children.add(parent);
        }
    }
}
