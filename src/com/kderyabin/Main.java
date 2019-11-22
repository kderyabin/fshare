package com.kderyabin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream("views/frame.fxml"));//FXMLLoader.load(getClass().getResource("views/frame.fxml"));
        Controller ctl = loader.getController();
        System.out.println(ctl.getClass());

        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

        ctl.initMenuActions();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
