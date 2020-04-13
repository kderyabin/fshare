package com.kderyabin;

import com.kderyabin.services.NavigateService;
import com.kderyabin.viewmodels.MainViewModel;
import com.kderyabin.views.*;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.spring.MvvmfxSpringApplication;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootApplication
public class Main extends MvvmfxSpringApplication {

    private static Logger LOG = LoggerFactory.getLogger(Main.class);

    private  ApplicationContext context;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage ;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void startMvvmfx(Stage primaryStage) {
        stage = primaryStage;
        Locale.setDefault(Locale.ENGLISH);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("default");
        MvvmFX.setGlobalResourceBundle(resourceBundle);

        final ViewTuple<MainView, MainViewModel> tuple
                = FluentViewLoader.fxmlView(MainView.class).load();

        registerNavigation(tuple.getCodeBehind().getContent());

        Parent root = tuple.getView();
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        LOG.info("Bound size:" + bounds.toString());
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
    }

    /**
     * Initialize views for navigation.
     * @param contentArea Pane in which the content is displayed.
     */
    private void registerNavigation(Pane contentArea) {
        NavigateService navigateService = context.getBean(NavigateService.class);
        navigateService.register("home", HomeView.class);
        navigateService.register("start", StartView.class);
        navigateService.register("board-form", BoardFormView.class);
        navigateService.register("board-items", BoardItemsView.class);
        navigateService.setContent(contentArea);
    }
}
