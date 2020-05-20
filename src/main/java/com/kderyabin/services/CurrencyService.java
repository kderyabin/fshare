package com.kderyabin.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CurrencyService {

    /**
     * @return A list of available currencies sorted by name
     */
    public static List<Currency> getAllCurrencies()
    {
        Set<Currency> result = new HashSet<>();
        Locale[] locs = Locale.getAvailableLocales();

        for(Locale loc : locs) {
            try {
                Currency currency = Currency.getInstance( loc );

                if ( currency != null ) {
                    result.add( currency );
                }
            } catch(Exception exc)
            {
                // Locale not found
            }
        }
        List<Currency> list = new ArrayList<>(result);
        list.sort(Comparator.comparing(Currency::getDisplayName));

        return list;
    }

    /**
     * Get Currency instance from currency code
     * @param code Currency 3 letters code (the ISO 4217 code of the currency)
     * @return  Currency instance
     */
    public static Currency getCurrency(String code){
        return Currency.getInstance(code);
    }
}
