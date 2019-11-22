package com.kderyabin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    MenuItem menuExit;

    public void initMenuActions(){
        System.out.println(">>>>> exit menu: " + menuExit.getText());
        menuExit.setOnAction( actionEvent -> Platform.exit());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
