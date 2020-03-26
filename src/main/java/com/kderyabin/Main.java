package com.kderyabin;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static Scene scene;
    private static Pane content;

    @Override
    public void start(Stage primaryStage) throws Exception {
        scene = new Scene(loadFXML("main"));
        content = (Pane)scene.lookup("#content");
        scene.getStylesheets().add(
                this.getClass().getResource("assets/style.css").toExternalForm()
        );
        // prefHeight="1024" prefWidth="786"
        scene.getRoot().prefHeight(1024);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * Method used to navigate between pages resets the root component.
     * @param fxml  Fxml file name.
     * @throws IOException See loadFXML.

     static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    */
    /**
     * Method used to navigate between pages resets components of the content area.
     * @param fxml  Fxml file name.
     * @throws IOException See loadFXML.
     */
    public static void setContent(String fxml) throws IOException {
        ObservableList<Node> children = content.getChildren();
        children.remove(0, children.size());
        children.add(loadFXML(fxml));
    }
    /**
     *  Load fxml file.
     * @param fxml  Fxml file name without the extension.
     * @return  Parent
     * @throws IOException Thrown if fxml file is not found.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
