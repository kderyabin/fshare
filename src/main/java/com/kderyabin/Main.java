package com.kderyabin;

import com.kderyabin.dao.PersonRepository;
import com.kderyabin.services.NavigateService;
import com.kderyabin.viewmodels.MainViewModel;
import com.kderyabin.views.BoardFormView;
import com.kderyabin.views.HomeView;
import com.kderyabin.views.MainView;
import com.kderyabin.views.StartView;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.MvvmFX;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.spring.MvvmfxSpringApplication;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
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
        // set dimension
        root.prefHeight(1024);
        root.prefWidth(786);
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
        navigateService.setContent(contentArea);
    }
}
