package com.kderyabin;

import com.kderyabin.viewmodels.MainViewModel;
import com.kderyabin.views.BoardFormView;
import com.kderyabin.views.MainView;
import com.kderyabin.views.StartView;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene scene;
    private static Pane content;
    private static MainView appView;


    public void start(Stage primaryStage) throws Exception {
        final ViewTuple<MainView, MainViewModel> tuple
                = FluentViewLoader.fxmlView(MainView.class).load();

        appView = tuple.getCodeBehind();
        Parent root = tuple.getView();
        // set dimension
        root.prefHeight(1024);
        root.prefWidth(786);
        scene = new Scene(root);
        scene.getStylesheets().add(
                this.getClass().getResource("assets/style.css").toExternalForm()
        );

        primaryStage.setTitle("Share");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Loads UI components into content area.
     *
     * @param viewClass View class to load into content area, something like StartView.class
     */
    public static void setContent(String viewClass) throws Exception {

        String clazz = viewClass.substring(0, 1).toUpperCase() + viewClass.substring(1) + "View";
        Parent parent;
        switch (clazz) {
            case "MainView":
                parent = FluentViewLoader.fxmlView(MainView.class).load().getView();
                break;
            case "BoardFormView":
                parent =FluentViewLoader.fxmlView(BoardFormView.class).load().getView();
                break;
            case "StartView":
                parent =FluentViewLoader.fxmlView(StartView.class).load().getView();
                break;
            default:
                throw new Exception("Undefined view with name " + clazz);
        }
        appView.setContent(parent);
    }
}
