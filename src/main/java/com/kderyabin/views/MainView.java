package com.kderyabin.views;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
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

    /**
     * Snackbar provides brief messages about app processes at the bottom of the screen.
     */
    JFXSnackbar info;
    /**
     * Snackbar with action button provides brief messages about app processes at the bottom of the screen.
     */
    JFXSnackbar infoDismiss;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        info = new JFXSnackbar(root);
        infoDismiss = new JFXSnackbar(root);
        info.setPrefWidth(300);
        infoDismiss.setPrefWidth(300);
        notificationCenter.subscribe(Notification.INFO, ((key, payload) -> {
            String message = (String) payload[0];
            displayInfo(message);
        }));
        notificationCenter.subscribe(Notification.INFO_DISMISS, (key, payload) -> {
            String message = (String) payload[0];
            displayDismissInfo(message);
        });
    }

    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public Pane getContent() {
        return content;
    }

    public void displayInfo(String resourceKey) {
        if(info.isVisible()) {
            info.close();
        }
        String message = resources.getString(resourceKey);
        info.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message),
                Duration.millis(1500), null)
        );
    }

    public void displayDismissInfo(String resourceKey) {
        if( infoDismiss.isVisible()){
            infoDismiss.close();
        }
        String message = resources.getString(resourceKey);
        infoDismiss.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message, "OK", action -> infoDismiss.close()),
                Duration.INDEFINITE, null)
        );
    }
}
