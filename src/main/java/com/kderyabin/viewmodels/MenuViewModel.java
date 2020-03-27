package com.kderyabin.viewmodels;

import de.saxsys.mvvmfx.ViewModel;
import javafx.application.Platform;

public class MenuViewModel implements ViewModel {
    public void exit() {
        Platform.exit();
    }
}
