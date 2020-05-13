package com.kderyabin.services;

import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardBalanceTest {
    final private Logger LOG = LoggerFactory.getLogger(BoardBalanceTest.class);

    @Test
    void shareBoardTotal() {
        // Test data
        PersonModel person1 = new PersonModel(1,"Traveller1");
        PersonModel person2 = new PersonModel(2,"Traveller2");
        PersonModel person3 = new PersonModel(3,"Traveller3");
        PersonModel person4 = new PersonModel(4,"Traveller4");

        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("100"),1, person1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("50"),1, person2);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("0"),1, person3);
        BoardPersonTotal traveller4 = new BoardPersonTotal(new BigDecimal("300"),1, person4);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);
        data.add(traveller4);
        BoardBalance balance = new BoardBalance();
        balance.setData(data);
        balance.shareBoardTotal();
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        LOG.debug(result.toString());

        assertEquals(0,result.get(person1).get(person4).compareTo(new BigDecimal("12.50")));
        assertEquals(0,result.get(person2).get(person4).compareTo(new BigDecimal("62.50")));
        assertEquals(0,result.get(person3).get(person4).compareTo(new BigDecimal("112.50")));
    }
}
