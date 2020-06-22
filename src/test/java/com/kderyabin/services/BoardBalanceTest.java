package com.kderyabin.services;

import com.kderyabin.partagecore.model.BoardPersonTotal;
import com.kderyabin.partagecore.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        balance.setTotals(data);
        balance.shareBoardTotal();
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        LOG.debug(result.toString());

        assertEquals(0,result.get(person1).get(person4).compareTo(new BigDecimal("12.50")));
        assertEquals(0,result.get(person2).get(person4).compareTo(new BigDecimal("62.50")));
        assertEquals(0,result.get(person3).get(person4).compareTo(new BigDecimal("112.50")));
    }
    @Test
    void shareBoardTotalSeries2() {
        // Test data
        PersonModel person1 = new PersonModel(1,"Traveller1");
        PersonModel person2 = new PersonModel(2,"Traveller2");
        PersonModel person3 = new PersonModel(3,"Traveller3");

        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("110"),1, person1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("0"),1, person2);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("70"),1, person3);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);

        BoardBalance balance = new BoardBalance();
        balance.setTotals(data);
        balance.shareBoardTotal();
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        // Person 1 Total amount owed to other
        Optional<BigDecimal> sumPerson1 = result.get(person1).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        // Person 2 Total amount owed to other
        Optional<BigDecimal> sumPerson2 = result.get(person2).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        // Person 3 Total amount owed to other
        Optional<BigDecimal> sumPerson3 = result.get(person3).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));

        assertEquals(0,result.get(person2).get(person1).compareTo(new BigDecimal("50")));
        assertEquals(0,result.get(person2).get(person3).compareTo(new BigDecimal("10")));
        assertEquals(new BigDecimal("0"), sumPerson1.get());
        assertEquals(new BigDecimal("0"), sumPerson3.get());
        assertEquals(new BigDecimal("60.00" ), sumPerson2.get());
    }
    @Test
    void shareBoardTotalSeries3() {
        // Test data
        PersonModel person1 = new PersonModel(1,"Traveller1");
        PersonModel person2 = new PersonModel(2,"Traveller2");
        PersonModel person3 = new PersonModel(3,"Traveller3");

        List<BoardPersonTotal> data = new ArrayList<>();
        BoardPersonTotal traveller1 = new BoardPersonTotal(new BigDecimal("110"),1, person1);
        BoardPersonTotal traveller2 = new BoardPersonTotal(new BigDecimal("70"),1, person2);
        BoardPersonTotal traveller3 = new BoardPersonTotal(new BigDecimal("0"),1, person3);
        data.add(traveller1);
        data.add(traveller2);
        data.add(traveller3);

        BoardBalance balance = new BoardBalance();
        balance.setTotals(data);
        balance.shareBoardTotal();
        Map<PersonModel, Map<PersonModel, BigDecimal>> result = balance.getShare();

        // Person 1 Total amount owed to other
        Optional<BigDecimal> sumPerson1 = result.get(person1).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        // Person 2 Total amount owed to other
        Optional<BigDecimal> sumPerson2 = result.get(person2).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
        // Person 3 Total amount owed to other
        Optional<BigDecimal> sumPerson3 = result.get(person3).values().stream().reduce((bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));

        assertEquals(0,result.get(person3).get(person1).compareTo(new BigDecimal("50")));
        assertEquals(0,result.get(person3).get(person2).compareTo(new BigDecimal("10")));
        assertEquals(new BigDecimal("0"), sumPerson1.get());
        assertEquals(new BigDecimal("0"), sumPerson2.get());
        assertEquals(new BigDecimal("60.00" ), sumPerson3.get());
    }
    private void printArrays(Map<PersonModel, Map<PersonModel, BigDecimal>> result){
        result.forEach( (debtor, debts ) -> {
            System.out.print(debtor.getName() + " owes to\t");
            debts.forEach( (creditor, amount) -> {
                System.out.print("|\t" + creditor.getName() + " ( "+ amount+" )\t" );
            });
            System.out.println("|");
        });
    }
}
