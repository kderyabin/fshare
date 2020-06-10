package com.kderyabin.views;

import com.kderyabin.util.GUIHelper;
import com.kderyabin.viewmodels.SettingsViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.Locale;


@Component
@Scope("prototype")
public class SettingsView implements FxmlView<SettingsViewModel> {

    @FXML
    public ComboBox<Currency> currencyList;
    @FXML
    public ComboBox<Locale> langList;

    @InjectViewModel
    private SettingsViewModel viewModel;


    public void initialize() {

        // Currencies' list and preselection
        currencyList.setConverter(GUIHelper.getCurrencyStringConverter());
        currencyList.itemsProperty().bind(viewModel.currenciesProperty());
        // Auto update selected currency in viewModel
        currencyList.valueProperty().bindBidirectional(viewModel.currencyProperty());

        langList.setConverter(GUIHelper.getLangStringConverter());
        langList.itemsProperty().bind(viewModel.langsProperty());
        // Auto update selected language in viewModel
        langList.valueProperty().bindBidirectional(viewModel.langProperty());
    }
}
