package com.kderyabin.services;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class BoardBalance {

    private StorageManager storageManager;
    /**
     * Active board
     */
    private BoardModel board;
    /**
     * List of totals per participants
     */
    private List<BoardPersonTotal> data = new ArrayList<>();
    /**
     * Balance per participant is based on the average amount.
     * Negative balance means the person owns money.
     * Positive balance means the person payed too much.
     */
    Map<Integer, BigDecimal> balances = new HashMap<>();

    /**
     * Total amount for the board.
     */
    private BigDecimal total ;
    /**
     * Fare amount per person derived from total of the board.
     */
    private BigDecimal average;

    /**
     * Amount per person to be payed to any other participant.
     * key -> personId
     * value -> map where the key is a personId and the value is an amount
     */
    Map<Integer, Map<Integer, BigDecimal>> share = new HashMap<>();

    public BoardBalance() {}

    /**
     * Must be called to initialize the data.
     */
    public void init(){
        if(getBoard() == null) {
            throw new RuntimeException("Board must by initialized prior to call BoardBalance.init");
        }
        setData(storageManager.getBoardPersonTotal(board.getId()));
    }

    public void calculateTotal() {
        total = data.stream().map(BoardPersonTotal::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public void calculateAverage(){
        average = total.divide(BigDecimal.valueOf(data.size()), 2, RoundingMode.CEILING);
    }

    /**
     * Calculate balance for every person.
     */
    public void calculateBalance(){
        for( BoardPersonTotal person: data) {
            BigDecimal balance = person.getTotal().subtract(average);
            balances.put(person.getPersonId(), balance);
        }
    }
    /**
     * Calculate amount of debt to share.
     */
    public void shareBoardTotal() {
        if(balances.size() == 0 ) {
            return;
        }
        // share amounts between participants
        for( BoardPersonTotal person: data) {

            BigDecimal balance = balances.get(person.getPersonId());
            // Negative balance means person owns money.
            // So we are going to dispatch own amount between participants having payed too much.
            Map<Integer, BigDecimal> line = new HashMap<>();
            share.put(person.getPersonId(), line);
            for( BoardPersonTotal friend: data) {
                Integer friendId = friend.getPersonId();
                // skip current person
                if(friendId.equals(person.getPersonId())) {
                    line.put(friendId, new BigDecimal("0"));
                    continue;
                }
                BigDecimal friendBalance = balances.get(friendId);
                // Does the friend need to be payed back?
                if(friendBalance.compareTo(BigDecimal.ZERO) > 0) {

                    if(friendBalance.compareTo(balance.abs()) > 0){
                        // The debt is less then amount payed by the friend so it can be sold partially
                        // Ex.: 60 + (-20) = 40
                        line.put(friendId, balance.abs());
                        // update friend's balance
                        balances.put(friendId, friendBalance.add(balance));
                        balance = new BigDecimal("0");
                    } else {
                        // The debt is greater then amount payed by the friend and can be sold totally.
                        // Ex.: 10 + (-30)
                        line.put(friendId, friendBalance);
                        // update balance
                        balance = friendBalance.add(balance);
                        balances.put(friendId, new BigDecimal("0"));
                    }
                } else {
                    line.put(friendId, new BigDecimal("0"));
                }
            }
            // update the balance of the current person
            balances.put(person.getPersonId(), balance);
        }
    }
    // Getters / Setters
    public StorageManager getStorageManager() {
        return storageManager;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public BoardModel getBoard() {
        return board;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }

    public List<BoardPersonTotal> getData() {
        return data;
    }

    public void setData(List<BoardPersonTotal> data) {
        this.data = data;
        if(data.size() > 0) {
            calculateTotal();
            calculateAverage();
            calculateBalance();
        }
    }

    public Map<Integer, Map<Integer, BigDecimal>> getShare() {
        return share;
    }

    public void setShare(Map<Integer, Map<Integer, BigDecimal>> share) {
        this.share = share;
    }

    public Map<Integer, BigDecimal> getBalances() {
        return balances;
    }

    public void setBalances(Map<Integer, BigDecimal> balances) {
        this.balances = balances;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }
}
