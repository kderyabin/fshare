package com.kderyabin.services;

import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Locale;

/**
 * User settings
 */
@Service
public class SettingsService {
    /**
     * Default currency.
     * Preset fro every board. Can be changed.
     */
    private Currency currency = Currency.getInstance(Locale.getDefault());

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setCurrency(String code) {
        this.currency = Currency.getInstance(code);
    }
}
