package com.kderyabin.viewmodels;

import com.kderyabin.model.SettingModel;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.RunService;
import com.kderyabin.services.SettingsService;
import com.kderyabin.services.StorageManager;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Component
@Scope("prototype")
public class SettingsViewModel implements ViewModel {

    final private Logger LOG = LoggerFactory.getLogger(SettingsViewModel.class);
    // Dependencies
    private RunService runService;
    private SettingsService settingsService;
    private StorageManager storageManager;
    private NavigateServiceInterface navigation;
    private NotificationCenter notificationCenter;

    // Properties
    /**
     * List of available currencies
     */
    private ListProperty<Currency> currencies = new SimpleListProperty<>(FXCollections.observableArrayList());
    /**
     * Current default currency
     */
    private ObjectProperty<Currency> currency = new SimpleObjectProperty<>();
    /**
     * List of available languages
     */
    private ListProperty<Locale> langs = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * List of registered settings.
     */
    private List<SettingModel> settings = new ArrayList<>();

    /**
     * Current default language.
     */
    private ObjectProperty<Locale> lang = new SimpleObjectProperty<>();

    public void initialize() {
        currencies.addAll(SettingsService.getAllCurrencies());
        langs.addAll(settingsService.getAvailableLanguages());
        setCurrency(settingsService.getCurrency());
        setLang(settingsService.getLanguage());
        CompletableFuture.runAsync(this::loadSettings, runService.getExecutorService());
    }

    /**
     * Load settings from DB
     */
    private void loadSettings() {
        settings = storageManager.getSettings();
        LOG.debug("End Loading settings from DB");
    }

    public void save() {
        // Get existing ot create a new one
        SettingModel currency = settings
                .stream()
                .filter(s -> s.getName().equals(SettingsService.CURRENCY_NAME))
                .findFirst()
                .orElse(new SettingModel(SettingsService.CURRENCY_NAME, ""));
        currency.setValue(getCurrency().getCurrencyCode());
        // Get existing ot create a new one
        SettingModel language = settings
                .stream()
                .filter(s->s.getName().equals(SettingsService.LANG_NAME))
                .findFirst()
                .orElse(new SettingModel(SettingsService.LANG_NAME, ""));
        language.setValue(getLang().getLanguage());
        try{
            storageManager.save(Arrays.asList(currency, language));
            notificationCenter.publish(Notification.INFO, "msg.settings_saved_success");
            // Update application settings
            settingsService.setLanguage(getLang());
            settingsService.setCurrency(getCurrency());
            // And application language
            Locale.setDefault(getLang());
            // Can be null in unit tests.
            if (null != navigation) {
                // @TODO: reload the stage content
            }
        } catch (Exception e) {
            notificationCenter.publish( Notification.INFO_DISMISS, "msg.generic_error");
        }
    }

    // Getters / Setters

    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }
    @Autowired
    public void setNavigation(NavigateServiceInterface navigation) {
        this.navigation = navigation;
    }
    @Autowired
    public void setNotificationCenter(NotificationCenter notificationCenter) {
        this.notificationCenter = notificationCenter;
    }

    public ObservableList<Currency> getCurrencies() {
        return currencies.get();
    }

    public ListProperty<Currency> currenciesProperty() {
        return currencies;
    }

    public void setCurrencies(ObservableList<Currency> currencies) {
        this.currencies.set(currencies);
    }

    public Currency getCurrency() {
        return currency.get();
    }

    public ObjectProperty<Currency> currencyProperty() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency.set(currency);
    }

    public ObservableList<Locale> getLangs() {
        return langs.get();
    }

    public ListProperty<Locale> langsProperty() {
        return langs;
    }

    public void setLangs(ObservableList<Locale> langs) {
        this.langs.set(langs);
    }

    public Locale getLang() {
        return lang.get();
    }

    public ObjectProperty<Locale> langProperty() {
        return lang;
    }

    public void setLang(Locale lang) {
        this.lang.set(lang);
    }


}
