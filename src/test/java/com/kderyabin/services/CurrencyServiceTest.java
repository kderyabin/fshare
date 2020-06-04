package com.kderyabin.services;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Currency;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    private final Logger LOG = LoggerFactory.getLogger(CurrencyServiceTest.class);
    @Test
    public void getAllCurrencies() {
        List<Currency> result = CurrencyService.getAllCurrencies();
        LOG.debug("Currencies: " + result.toString());
        assertFalse(result.isEmpty());
    }
}
