package com.kderyabin.viewmodels;

import com.kderyabin.services.NavigateServiceInterface;
import de.saxsys.mvvmfx.ViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardViewModel implements ViewModel {

    @Autowired
    NavigateServiceInterface navigation;

    public void goBack() {
        try {
            navigation.navigate("start");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
