package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class BoardModel {

    private Integer id;

    private String name;

    private String description;

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
}
