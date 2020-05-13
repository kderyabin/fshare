package com.kderyabin.services;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.PersonModel;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
@Service
@Scope("prototype")
public class BoardBalance {

    final private Logger LOG = LoggerFactory.getLogger(BoardBalance.class);
    /**
     * List of totals per participants
     */
    private List<BoardPersonTotal> data = new ArrayList<>();

    /**
     * Total amount for the board.
     */
    private BigDecimal total;
    /**
     * Fare amount per person derived from total of the board.
     */
    private BigDecimal average;

    /**
     * Amount per person to be payed to any other participant.
     * key -> PersonModel
     * value -> map where the key is a PersonModel to whome money are owned and the value is an amount
     */
    Map<PersonModel, Map<PersonModel, BigDecimal>> share = new HashMap<>();

    /**
     * To initialize the BoardBalance call in the following order
     * setData()
     * init()
     */
    public BoardBalance() {
    }

    public void calculateTotal() {
        total = data.stream().map(BoardPersonTotal::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void calculateAverage() {
        average = total.divide(BigDecimal.valueOf(data.size()), 2, RoundingMode.CEILING);
    }

    /**
     * Pushes board average and total into this.data entries
     */
    private void populateParticipants() {
        for (BoardPersonTotal person : data) {
            BigDecimal balance = person.getTotal().subtract(average);
            person.setBalance(balance);
            person.setBoardAverage(average);
        }
    }

    public void init() {
        if (data.size() > 0) {
            calculateTotal();
            calculateAverage();
            populateParticipants();
        }
    }

    /**
     * Balance per participant based on the average amount.
     * Negative balance means the person owns money.
     * Positive balance means the person payed too much.
     */
    public Map<PersonModel, BigDecimal> getBalancePerPerson() {

        Map<PersonModel, BigDecimal> balances = new HashMap<>();
        data.stream().parallel().forEach( person -> balances.put(person.getPerson(), person.getBalance()));
//        for (BoardPersonTotal person : data) {
//            BigDecimal balance = person.getTotal().subtract(average);
//            balances.put(person.getPerson(), balance);
//        }
        return balances;
    }

    /**
     * Calculate amount of debt to share.
     */
    public void shareBoardTotal() {
        Map<PersonModel, BigDecimal> balances = getBalancePerPerson();
        LOG.debug(">>> balances start" + balances.toString());
        if (balances.size() == 0) {
            return;
        }
        // share amounts between participants
        for (BoardPersonTotal person : data) {

            BigDecimal balance = balances.get(person.getPerson());
            // Negative balance means person owns money.
            // So we are going to dispatch own amount between participants having payed too much.
            Map<PersonModel, BigDecimal> line = new HashMap<>();
            share.put(person.getPerson(), line);
            for (BoardPersonTotal mate : data) {
                PersonModel friend = mate.getPerson();
                // skip current person
                if (friend.equals(person.getPerson())) {
                    line.put(friend, new BigDecimal("0"));
                    continue;
                }
                BigDecimal friendBalance = balances.get(friend);
                // Does the mate need to be payed back?
                if (friendBalance.compareTo(BigDecimal.ZERO) > 0) {

                    if (friendBalance.compareTo(balance.abs()) > 0) {
                        // The debt is less then amount payed by the mate so it can be sold partially
                        // Ex.: 60 + (-20) = 40
                        line.put(friend, balance.abs());
                        // update mate's balance
                        balances.put(friend, friendBalance.add(balance));
                        balance = new BigDecimal("0");
                    } else {
                        // The debt is greater then amount payed by the mate and can be sold totally.
                        // Ex.: 10 + (-30)
                        line.put(friend, friendBalance);
                        // update balance
                        balance = friendBalance.add(balance);
                        balances.put(friend, new BigDecimal("0"));
                    }
                } else {
                    line.put(friend, new BigDecimal("0"));
                }
            }
            // update the balance of the current person
            balances.put(person.getPerson(), balance);
        }
        LOG.debug(">>> balances end" + balances.toString());
    }

    public List<BoardPersonTotal> getData() {
        return data;
    }

    public void setData(List<BoardPersonTotal> data) {
        this.data = data;
        init();
    }

    public Map<PersonModel, Map<PersonModel, BigDecimal>> getShare() {
        return share;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getAverage() {
        return average;
    }

}
