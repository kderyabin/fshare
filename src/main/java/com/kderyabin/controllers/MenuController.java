package com.kderyabin.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class MenuController {

    @FXML
    void exit() {
        Platform.exit();
    }
}
