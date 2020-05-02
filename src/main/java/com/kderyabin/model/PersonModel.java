package com.kderyabin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
public class PersonModel {

    private Integer id;
    private String name;
    @ToString.Exclude
    private List<BoardModel> boards = new ArrayList<>();
    @ToString.Exclude
    private List<BoardItemModel> items = new ArrayList<>();

    public PersonModel() {
    }

    public PersonModel(String name) {
        this.name = name;
    }

    public boolean addBoard(BoardModel boardModel) {
        return boards.add(boardModel);
    }

    public boolean removeBoard(BoardModel boardModel) {
        return boards.remove(boardModel);
    }

    public void addItem(BoardItemModel item) {
        items.add(item);
    }

    public void removedItem(BoardItemModel item){
        items.remove(item);
    }
}
