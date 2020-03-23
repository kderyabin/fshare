package com.kderyabin.controllers;

import com.kderyabin.Main;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * @author Konstantin Deryabin
 * @date 12/03/2020
 */
public class StartController {

    @FXML
    public void btnClick(MouseEvent mouseEvent) throws IOException {
        Main.setContent("board-form");
    }
}
