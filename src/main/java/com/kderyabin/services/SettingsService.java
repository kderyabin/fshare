package com.kderyabin.services;

import com.kderyabin.partagecore.model.SettingModel;
import com.kderyabin.partagecore.storage.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * User settings
 */
@Service
public class SettingsService {

    final private Logger LOG = LoggerFactory.getLogger(SettingsService.class);

    /**
     * Fallback currency in case something goes wrong with currency generation.
     */
    final private static Currency FALLBACK_CURRENCY = Currency.getInstance(Locale.getDefault());

    final public static String CURRENCY_NAME = "currency";
    final public static String LANG_NAME = "lang";
    /**
     * Default currency.
     */
    private Currency currency = Currency.getInstance(Locale.getDefault());
    /**
     * Language Locale without the country code.
     */
    private Locale language = new Locale(Locale.getDefault().getLanguage());

    RunService runService;
    StorageManager storageManager;


    /**
     * Returns list of available application languages
     *
     * @return List of available application languages.
     */
    public List<Locale> getAvailableLanguages() {
        List<Locale> list = new ArrayList<>();
        list.add(Locale.ENGLISH);
        list.add(Locale.FRENCH);

        return list;
    }

    /**
     * Loads user settings from DB.
     */
    public void load() {
        LOG.info("Loading settings from DB");
        LOG.debug("Requesting DB");
        List<SettingModel> settings = storageManager.getSettings();
        if (!settings.isEmpty()) {
            settings.forEach(s -> {
                switch (s.getName()) {
                    case CURRENCY_NAME:
                        setCurrency(getCurrencyFromCode(s.getValue()));
                        break;
                    case LANG_NAME: {
                        setLanguage(s.getValue());
                    }
                }
            });
        }
        LOG.debug("End Loading settings from DB");
    }


    /**
     * Get Currency instance from currency code
     *
     * @param code Currency 3 letters code (the ISO 4217 code of the currency)
     * @return Currency instance
     */
    public static Currency getCurrencyFromCode(String code) {
        try {
            return Currency.getInstance(code);
        } catch (Exception e) {
            // something went wrong. Return default currency.
            return FALLBACK_CURRENCY;
        }
    }

    /**
     * @return A list of available currencies sorted by name
     */
    public static List<Currency> getAllCurrencies() {
        Set<Currency> result = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for (Locale loc : locs) {
            try {
                Currency currency = Currency.getInstance(loc);

                if (currency != null) {
                    result.add(currency);
                }
            } catch (Exception exc) {
                // Locale not found
            }
        }
        List<Currency> list = new ArrayList<>(result);
        list.sort(Comparator.comparing(Currency::getDisplayName));

        return list;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setCurrency(String code) {
        this.currency = getCurrencyFromCode(code);
    }

    public Locale getLanguage() {
        return language;
    }

    public void setLanguage(Locale language) {
        this.language = language;
    }

    /**
     * Construct a locale from a language code
     *
     * @param code An ISO 639-1 language code
     */
    public void setLanguage(String code) {
        this.language = new Locale(code);
    }

    public RunService getRunService() {
        return runService;
    }

    @Autowired
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

}
