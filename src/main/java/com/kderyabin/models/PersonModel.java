package com.kderyabin.models;

import lombok.ToString;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Table(name = "person")
public class PersonModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ToString.Exclude
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            mappedBy = "participants")
    private Set<BoardModel> boards = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BoardItemModel> items = new LinkedHashSet<>();

    public PersonModel() {
    }

    public PersonModel(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BoardModel> getBoards() {
        return boards;
    }

    public void setBoards(Set<BoardModel> boards) {
        this.boards = boards;
    }

    public boolean addBoard(BoardModel boardModel) {
        return boards.add(boardModel);
    }

    public boolean removeBoard(BoardModel boardModel) {
        return boards.remove(boardModel);
    }

    public Set<BoardItemModel> getItems() {
        return items;
    }

    public void setItems(Set<BoardItemModel> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonModel that = (PersonModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
