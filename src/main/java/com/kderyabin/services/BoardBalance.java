package com.kderyabin.services;

import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.PersonModel;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *
 */
@ToString
@Service
@Scope("prototype")
public class BoardBalance {

    final private Logger LOG = LoggerFactory.getLogger(BoardBalance.class);
    /**
     * List of totals per participants
     */
    private List<BoardPersonTotal> totals = new ArrayList<>();

    /**
     * Total amount of the board.
     */
    private BigDecimal total;
    /**
     * Average amount of the board derived from total amount.
     */
    private BigDecimal average;

    /**
     * Map containing a list of participants and amount owed to any other participant
     * The key of the map is a participant (PersonModel) and a value is another map
     * where the key is a participant (PersonModel) to whom money are owned and the value is an amount.
     */
    private Map<PersonModel, Map<PersonModel, BigDecimal>> share = new HashMap<>();

    /**
     * Calculates a total amount of the board.
     */
    public void calculateTotal() {
        total = totals.stream().map(BoardPersonTotal::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     *
     */
    public void calculateAverage() {
        average = total.divide(BigDecimal.valueOf(totals.size()), 2, RoundingMode.CEILING);
    }

    /**
     * Pushes board average and total into this.totals entries
     */
    private void populateParticipants() {
        for (BoardPersonTotal person : totals) {
            BigDecimal balance = person.getTotal().subtract(average);
            person.setBalance(balance);
            person.setBoardAverage(average);
        }
    }
    public boolean isEmpty() {
        return total == null || total.compareTo(BigDecimal.ZERO) == 0;
    }

    public void init() {
        if (totals.size() > 0) {
            calculateTotal();
            calculateAverage();
            if(!isEmpty()) {
                populateParticipants();
            }
        }
    }

    /**
     * Balance per participant based on the average amount.
     * Negative balance means the person owns money.
     * Positive balance means the person payed too much.
     */
    public Map<PersonModel, BigDecimal> getBalancePerPerson() {

        Map<PersonModel, BigDecimal> balances = new LinkedHashMap<>();
        totals.stream().parallel().forEach(person -> balances.put(person.getPerson(), person.getBalance()));
        return balances;
    }

    /**
     * Calculate amount of debt to share.
     * Produces  2 dimensional array where column header represents a creditor
     * and row header represents a debtor.
     * Ex.: given the balance for total amount 180 with average 60: Jane=50, Anna=-60, John=10
     * |        |   Jane    |   Anna    |   John    |
     * | Jane   |   0       |   0       |   0       |
     * | Anna   |   50      |   0       |   10      |
     * | John   |   0       |   0       |   0       |
     * Ann owes 50 to Jane and 10 to John
     */
    public void shareBoardTotal() {
        Map<PersonModel, BigDecimal> balances = getBalancePerPerson();
        LOG.debug(">>> balances mvvm" + balances.toString());
        if (isEmpty()) {
            return;
        }
        // share amounts between participants
        for (BoardPersonTotal person : totals) {

            BigDecimal personBalance = balances.get(person.getPerson());
            // Negative balance means person owns money.
            // So we are going to dispatch owed amount between participants having payed too much.
            Map<PersonModel, BigDecimal> line = new HashMap<>();
            share.put(person.getPerson(), line);
            for (BoardPersonTotal otherParticipant : totals) {
                PersonModel friend = otherParticipant.getPerson();
                // skip if current person does not owes money or it's the same person
                if (personBalance.compareTo(BigDecimal.ZERO) >= 0 || friend.equals(person.getPerson())) {
                    line.put(friend, new BigDecimal("0"));
                    continue;
                }
                BigDecimal friendBalance = balances.get(friend);
                // Does the otherParticipant need to be payed back?
                if (friendBalance.compareTo(BigDecimal.ZERO) > 0) {
                    // Yes. Needs to be refunded.
                    LOG.debug("Creditor: " + friendBalance + " Debtor: " + personBalance + "(" + personBalance.abs() +")");
                    if (friendBalance.compareTo(personBalance.abs()) > 0) {
                        LOG.debug("Debt can be refunded partially");
                        // The debt is less then amount payed by the otherParticipant so it can be sold partially
                        // Ex.: 60 + (-20) = 40
                        line.put(friend, personBalance.abs());
                        // update otherParticipant's balance
                        balances.put(friend, friendBalance.add(personBalance));
                        personBalance = new BigDecimal("0");
                    } else {
                        LOG.debug("Debt can be refunded totally");
                        // The debt is greater then amount payed by the otherParticipant and can be sold totally.
                        // Ex.: 10 + (-30)
                        line.put(friend, friendBalance);
                        // update balance
                        personBalance = friendBalance.add(personBalance);
                        balances.put(friend, new BigDecimal("0"));
                    }
                } else {
                    line.put(friend, new BigDecimal("0"));
                }
            }
            // update the balance of the current person
            balances.put(person.getPerson(), personBalance);

        }
        LOG.debug(">>> balances end" + balances.toString());
    }

    public List<BoardPersonTotal> getTotals() {
        return totals;
    }

    public void setTotals(List<BoardPersonTotal> totals) {
        this.totals = totals;
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
