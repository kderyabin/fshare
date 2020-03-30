package com.kderyabin.viewmodels;

import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartViewModel implements ViewModel {

    @Autowired
    NavigateServiceInterface navigation;

    public void editBoard() {
        // TODO: check if we can go back
        try {

            navigation.navigate("board-form");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
