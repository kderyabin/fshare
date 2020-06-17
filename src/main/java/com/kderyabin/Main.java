package com.kderyabin;

import com.kderyabin.services.SettingsService;
import com.kderyabin.mvvm.main.MainViewModel;
import com.kderyabin.mvvm.main.MainView;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.spring.MvvmfxSpringApplication;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootApplication
public class Main extends MvvmfxSpringApplication {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    private SettingsService settingsService;

    public static void main(String[] args) {
        LOG.info("Application started");
        launch(args);
    }

    @Override
    public void startMvvmfx(Stage primaryStage) {
        LOG.info("Start stage loading");
        settingsService.load();
        Locale.setDefault(settingsService.getLanguage());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("default");
        MvvmFX.setGlobalResourceBundle(resourceBundle);

        final ViewTuple<MainView, MainViewModel> tuple
                = FluentViewLoader.fxmlView(MainView.class).load();

        Parent root = tuple.getView();
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        // set dimension
        primaryStage.setHeight(bounds.getHeight() - 20);
        primaryStage.setWidth(bounds.getWidth() - 20);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                this.getClass().getResource("assets/style.css").toExternalForm()
        );
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/appIcon.png")));
        primaryStage.setTitle(resourceBundle.getString("window.title"));
        primaryStage.setScene(scene);
        primaryStage.show();
        LOG.info("End stage loading");
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }
}
