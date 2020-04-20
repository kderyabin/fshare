package com.kderyabin.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class List extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("List.fxml"));
        final Scene scene = new Scene(root, 600, 600, Color.WHITE);
        stage.setTitle("JFX ListView Demo ");
        scene.getStylesheets().add(getClass().getResource("css/jfoenix-components.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
