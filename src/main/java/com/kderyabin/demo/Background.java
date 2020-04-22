package com.kderyabin.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Background extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Background.fxml"));
        final Scene scene = new Scene(root);
        stage.setTitle("JFX ListView Demo ");
        scene.getStylesheets().add(getClass().getResource("../assets/style.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }
}
