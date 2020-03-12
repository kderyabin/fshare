package com.kderyabin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //FXMLLoader loader = new FXMLLoader();
        //loader.setLocation(getClass().getResource("views/main.fxml"));
//        Font.loadFont(
//                this.getClass().getResource("assets/fonts/Roboto-Light.ttf").toExternalForm(), 24
//        );
        Parent root = FXMLLoader.load(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(root);

        scene.getStylesheets().add(
                this.getClass().getResource("assets/style.css").toExternalForm()
        );
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
