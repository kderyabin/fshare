package com.kderyabin.views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.util.Notification;
import com.kderyabin.viewmodels.MainViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectResourceBundle;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainView implements FxmlView<MainViewModel>, Initializable {

    @FXML
    public Pane content;
    @FXML
    public Pane root;


    @InjectViewModel
    private MainViewModel viewModel;

    NotificationCenter notificationCenter;

    @InjectResourceBundle
    ResourceBundle resources;

    NavigateServiceInterface navigation;


    /**
     * Snackbar provides brief messages about app processes at the bottom of the screen.
     */
    JFXSnackbar info;
    /**
     * Snackbar with action button provides brief messages about app processes at the bottom of the screen.
     */
    JFXSnackbar infoDismiss;
    JFXSnackbar infoRaw;

    /**
     * Reference to currently shown snackbar.
     */
    private JFXSnackbar openedSnackbar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        info = new JFXSnackbar(root);
        infoDismiss = new JFXSnackbar(root);
        infoRaw = new JFXSnackbar(root);
        info.setPrefWidth(400);
        infoDismiss.setPrefWidth(400);
        infoRaw.setPrefWidth(400);
        notificationCenter.subscribe(Notification.INFO, ((key, payload) -> {
            String message = (String) payload[0];
            displayInfo(message);
        }));
        notificationCenter.subscribe(Notification.INFO_DISMISS, (key, payload) -> {
            String message = (String) payload[0];
            displayDismissInfo(message);
        });
        notificationCenter.subscribe(Notification.INFO_RAW_DISMISS, (key, payload) -> {
            String message = (String) payload[0];
            displayRawInfo(message);
        });
        navigation.setContent(content);
        if (viewModel.isHasBoards()) {
            navigation.navigate("home");
        } else {
            navigation.navigate("start");
        }
    }

    public void displayInfo(String resourceKey) {
        if (openedSnackbar != null && openedSnackbar.isVisible()) {
            openedSnackbar.close();
        }
        openedSnackbar = info;
        String message = resources.getString(resourceKey);
        info.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message),
                Duration.millis(1500), null)
        );
    }

    public void displayDismissInfo(String resourceKey) {
        if (openedSnackbar != null && openedSnackbar.isVisible()) {
            openedSnackbar.close();
        }
        openedSnackbar = infoDismiss;
        String message = resources.getString(resourceKey);
        infoDismiss.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message, "OK", action -> infoDismiss.close()),
                Duration.INDEFINITE, null)
        );
    }

    public void displayRawInfo(String message) {
        if (openedSnackbar != null && openedSnackbar.isVisible()) {
            openedSnackbar.close();
        }
        openedSnackbar = infoRaw;
        infoRaw.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message, "OK", action -> infoRaw.close()),
                Duration.INDEFINITE, null)
        );
    }

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public Pane getContent() {
        return content;
    }
}
