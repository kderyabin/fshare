package com.kderyabin.demo;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private static final String ITEM = "Item ";
    @FXML
    public JFXListView<Label> list;
    @FXML
    public Button button3D;
    @FXML
    public Button buttonExpand;
    @FXML
    public Button buttonCollapse;
    public Button buttonVGap;
    public Button buttonRemove;


    private int counter = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 10; i++) {
            list.getItems().add(new Label(ITEM + i));
        }

        button3D.setOnMouseClicked(e ->{
            list.depthProperty().set(++counter % 2);
        });
        buttonExpand.setOnMouseClicked(e -> {
            list.depthProperty().set(1);
            list.setExpanded(true);
        });
        buttonCollapse.setOnMouseClicked(e -> {
            list.depthProperty().set(1);
            list.setExpanded(false);
        });
        buttonVGap.setOnMouseClicked(e -> {
            System.out.println("Vertical gao value: " + list.getVerticalGap());
        });

        buttonRemove.setOnMouseClicked( e -> {
            int size = list.getItems().size();
            list.getItems().remove(size-1, size);
        });
    }
}
