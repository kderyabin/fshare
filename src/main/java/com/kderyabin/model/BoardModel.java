package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.*;

@ToString
@Getter
@Setter
public class BoardModel {

    private Integer id;

    private String name;

    private String description;

    private Currency currency;

    private Timestamp creation = new Timestamp(System.currentTimeMillis());

    private Timestamp update = new Timestamp(System.currentTimeMillis());
    @ToString.Exclude
    private List<PersonModel> participants = new ArrayList<>();
    @ToString.Exclude
    private List<BoardItemModel> items = new ArrayList<>();

    public BoardModel() {
    }

    public BoardModel(String name) {
        this.name = name;
    }

    public boolean addParticipant(PersonModel participant){
        participant.addBoard(this);
        return participants.add(participant);
    }
    public boolean removeParticipant(PersonModel participant){
        participant.removeBoard(this);
        return participants.remove(participant);
    }
    public void addItem(BoardItemModel item){
        items.add(item);
    }

    public void removeItem(BoardItemModel item){
        items.remove(item);
    }


    public String getCurrencyCode() {
        return currency.getCurrencyCode();
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
    public void setCurrency(String code) {
        try{
            currency = Currency.getInstance(code);
        } catch (Throwable e) {
            currency = Currency.getInstance(Locale.getDefault());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardModel that = (BoardModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creation, that.creation) &&
                Objects.equals(update, that.update) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creation, update, currency);
    }

}
