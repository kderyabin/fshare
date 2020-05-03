package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;


@ToString
@Getter
@Setter
public class BoardItemModel {
    private Integer id;
    private String title;
    private BigDecimal amount;
    private Date date = new Date(System.currentTimeMillis());
    private PersonModel person;
    private BoardModel board;

    public BoardItemModel() {
    }

    public BoardItemModel(String title) {
        this.title = title;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(String amount) {
        this.amount = new BigDecimal(amount);
    }
}

