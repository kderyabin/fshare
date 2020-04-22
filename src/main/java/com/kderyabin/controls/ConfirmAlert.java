package com.kderyabin.controls;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class ConfirmAlert extends Alert {

    public ConfirmAlert(String contentText){
        super(AlertType.CONFIRMATION, contentText);
        initStyle(StageStyle.UTILITY);
        setHeaderText(null);
    }


    public ConfirmAlert(String contentText, ButtonType... buttons) {
        super(AlertType.CONFIRMATION, contentText, buttons);
        initStyle(StageStyle.UTILITY);
        setHeaderText(null);
    }
}
