package com.kderyabin.demo;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private static final String ITEM = "Item ";
    @FXML
    public JFXListView<Label> boardsList;
    @FXML
    public BorderPane root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXListView<Label> list = new JFXListView<>();
        for (int i = 0; i < 4; i++) {
            list.getItems().add(new Label(ITEM + i));
        }
        list.depthProperty().set(1);
        list.setExpanded(true);
    }
}
