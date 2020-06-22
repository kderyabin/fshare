package com.kderyabin.mvvm.settings;

import com.kderyabin.partagecore.model.SettingModel;
import com.kderyabin.partagecore.storage.StorageManager;
import com.kderyabin.services.NavigateServiceInterface;
import com.kderyabin.services.RunService;
import com.kderyabin.services.SettingsService;
import com.kderyabin.util.Notification;
import de.saxsys.mvvmfx.MvvmFX;
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
    private Map<String, SettingModel> models = new HashMap<>();

    /**
     * Current default language.
     */
    private ObjectProperty<Locale> lang = new SimpleObjectProperty<>();

    public void initialize() {
        LOG.debug("Start initialize");
        currencies.addAll(SettingsService.getAllCurrencies());
        langs.addAll(settingsService.getAvailableLanguages());
        setCurrency(settingsService.getCurrency());
        setLang(settingsService.getLanguage());
        CompletableFuture.runAsync(this::loadSettings, runService.getExecutorService());
        LOG.debug("End initialize");
    }

    /**
     * Loads settings from DB
     */
    private void loadSettings() {
        LOG.debug("Start settings loading from DB");
        settings = storageManager.getSettings();
        // Put in a map for easy access
        if (!settings.isEmpty()) {
            settings.forEach(s -> models.put( s.getName(), s));
        }
        LOG.debug("End settings Loading from DB");
    }

    /**
     * Quits settings screen without saving.
     */
    public void quit() {
        if (null != navigation) {
            navigation.navigate("home");
        }
    }
    /**
     * Saves in DB updated settings and redirects to home page.
     */
    public void save() {
        LOG.debug("Start saving");
        ArrayList<SettingModel> updatable = getUpdatedSettings();
        if(updatable.size() > 0) {
            // Can throw exception in case of unique constraint violation
            try{
                storageManager.save(updatable);
                notificationCenter.publish(Notification.INFO, "msg.settings_saved_success");
                // Update application settings
                settingsService.setLanguage(getLang());
                settingsService.setCurrency(getCurrency());
                // And application language
                Locale.setDefault(getLang());
                // Reload bundle
                ResourceBundle resourceBundle = ResourceBundle.getBundle("default");
                MvvmFX.setGlobalResourceBundle(resourceBundle);
                // Can be null in unit tests.
                if (null != navigation) {
                    navigation.reloadMenu();
                    navigation.navigate("home");
                }
            } catch (Exception e) {
                LOG.info("Exception thrown: " + e.getMessage());
                notificationCenter.publish( Notification.INFO_DISMISS, "msg.generic_error");
            }
        } else {
            // nothing to do
            quit();
        }
    }

    /**
     * Prepares data for saving.
     * @return List of updated or new settings to register in DB.
     */
    public ArrayList<SettingModel> getUpdatedSettings() {
        ArrayList<SettingModel> updatable = new ArrayList<>();

        // Currency
        String currencyValue = getCurrency().getCurrencyCode();
        SettingModel currencyModel = models.get(SettingsService.CURRENCY_NAME);
        if(currencyModel == null) {
            // Create if does not exists
            currencyModel = new SettingModel(SettingsService.CURRENCY_NAME, currencyValue);
            updatable.add(currencyModel);
        } else if( !currencyModel.getValue().equals(currencyValue)) {
            // Update if value has changed
            currencyModel.setValue(currencyValue);
            updatable.add(currencyModel);
        }

        // Language
        String langValue = getLang().getLanguage();
        SettingModel langModel = models.get(SettingsService.LANG_NAME);
        if(langModel == null) {
            // Create if does not exists
            langModel = new SettingModel(SettingsService.LANG_NAME, langValue);
            updatable.add(langModel);
        } else if( !langModel.getValue().equals(langValue)) {
            // Update if value has changed
            langModel.setValue(langValue);
            updatable.add(langModel);
        }
        if( updatable.size() > 1 ) {
            LOG.debug(langModel.toString());
        }

        return updatable;
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
