package com.kderyabin.viewmodels;

import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MenuViewModel implements ViewModel {

    NavigateServiceInterface navigation;

    public NavigateServiceInterface getNavigation() {
        return navigation;
    }

    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }

    public void exit() {
        Platform.exit();
    }

    public void createNewBoard() throws Exception {
        navigation.navigate("board-form");
    }
}
