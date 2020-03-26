package com.kderyabin.controllers;

import com.kderyabin.Main;

import java.io.IOException;

public class BoardFormController {
    public void goBack() {
        try{
            Main.setContent("start");
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
