package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
public class BoardPersonTotal {

    private BigDecimal total;
    private Integer personId;
    private String personName;
    private Integer boardId;

    public BoardPersonTotal() {
    }

    public BoardPersonTotal(BigDecimal total, Integer personId, String personName, Integer boardId) {
        this.total = total;
        this.personId = personId;
        this.personName = personName;
        this.boardId = boardId;
    }
}
