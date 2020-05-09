package com.kderyabin.services;

import com.kderyabin.model.BoardPersonTotal;
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
        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("100"),1,"Traveller1", 1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("50"),2,"Traveller2", 1);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("0"),3,"Traveller3", 1);
        BoardPersonTotal traveller4 = new BoardPersonTotal(new BigDecimal("300"),4,"Traveller4", 1);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);
        data.add(traveller4);
        BoardBalance balance = new BoardBalance();
        balance.setData(data);
        balance.shareBoardTotal();
        Map<Integer, Map<Integer, BigDecimal>> result = balance.getShare();

        LOG.debug(result.toString());

        assertEquals(0,result.get(1).get(4).compareTo(new BigDecimal("12.50")));
        assertEquals(0,result.get(2).get(4).compareTo(new BigDecimal("62.50")));
        assertEquals(0,result.get(3).get(4).compareTo(new BigDecimal("112.50")));
    }
}
