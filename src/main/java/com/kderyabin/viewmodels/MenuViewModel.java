package com.kderyabin.viewmodels;

import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;
import org.springframework.stereotype.Component;

@Component
public class MenuViewModel implements ViewModel {
    public void exit() {
        Platform.exit();
    }
}
