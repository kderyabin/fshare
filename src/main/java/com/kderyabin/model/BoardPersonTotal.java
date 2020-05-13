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
    private Integer boardId;
    private PersonModel person;
    /**
     * Average amount for the board
     */
    private BigDecimal boardAverage;
    /**
     * Person balance
     * @see com.kderyabin.services.BoardBalance
     */
    private BigDecimal balance;

    public BoardPersonTotal() {
    }

    public BoardPersonTotal(BigDecimal total, Integer boardId, PersonModel person) {
        this.total = total;
        this.boardId = boardId;
        this.person = person;
    }

    public BoardPersonTotal(BigDecimal total, Integer personId, String personName, Integer boardId) {
        this.total = total;
        person = new PersonModel(personId, personName);
        this.boardId = boardId;
    }

    public String getName() {
       return person.getName();
    }
}
