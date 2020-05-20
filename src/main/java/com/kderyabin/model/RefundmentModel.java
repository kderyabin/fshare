package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Currency;

@ToString
@Getter
@Setter
public class RefundmentModel {

    private String debtor;
    private String creditor;
    private Double amount;
    private Currency currency;

    public String getDisplayAmount() {
        return amount + " " + currency.getCurrencyCode();
    }
}
