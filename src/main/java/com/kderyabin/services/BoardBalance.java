package com.kderyabin.services;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.BoardPersonTotal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
     * Total amount for the board.
     */
    private BigDecimal total;
    /**
     * Fare amount per person derived from total of the board.
     */
    private BigDecimal average;

    /**
     * Amount per person to be payed to each participant.
     * key -> personId
     * value -> map where the key is a personId and the value is an amount
     */
    Map<Integer, Map<Integer, BigDecimal>> share = new HashMap<>();

    public BoardBalance() {}

    public void initBalance(){
        if(getBoard() == null) {
            throw new RuntimeException("Board must by initialized prior to call BoardBalance.initBalance");
        }
        data = storageManager.getBoardPersonTotal(board.getId());
        total = data.stream().map( i -> i.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add);
        average = total.divide(BigDecimal.valueOf(data.size()));
    }

    public void shareBoardTotal() {
        // calculate balance for each person
        // Negative balance means the person owns money
        Map<Integer, BigDecimal> balances = new HashMap<>();
        for( BoardPersonTotal person: data) {
            BigDecimal balance = person.getTotal().subtract(average);
            balances.put(person.getPersonId(), balance);
        }
        // share amounts between participants
        for( BoardPersonTotal person: data) {

            BigDecimal balance = balances.get(person.getPersonId());
            if(balance.compareTo(BigDecimal.ZERO) >= 0) {
                // Balance OK.
                continue;
            }
            // Negative balance means person owns money.
            // So we are going to dispatch own amount between participants having payed too much.
            Map<Integer, BigDecimal> line = new HashMap<>();
            share.put(person.getPersonId(), line);
            for( BoardPersonTotal friend: data) {
                // skip current person
                if(friend.getPersonId().equals(person.getPersonId())) {
                    continue;
                }
                BigDecimal friendBalance = balances.get(friend.getPersonId());
                if(friendBalance.compareTo(BigDecimal.ZERO) > 0) {
                    // Friend needs to be payed back
                    // Ex.: 10 + (-30) = -20, 60 + (-30) = 30
                    if(friendBalance.compareTo(balance.abs()) > 0){
                        // Ex.: 60 + (-30) = 30
                        line.put(friend.getPersonId(), balance.abs());
                        friendBalance = friendBalance.add(balance);
                        balance = new BigDecimal("0");
                    } else {
                        // Ex.: 10 + (-30)
                        line.put(friend.getPersonId(), friendBalance);
                        balance = friendBalance.add(balance);
                        friendBalance = new BigDecimal("0");
                    }
                }
            }
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
    }
}
