package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
public class RefundmentModel {

    private String debtor;
    private String creditor;
    private Double amount;
}
