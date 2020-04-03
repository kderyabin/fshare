package com.kderyabin.models;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "person")
public class PersonModel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToMany(mappedBy = "participants")
    private Set<BoardModel> boards = new LinkedHashSet<>();

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
        boardModel.addParticipant(this);
        return boards.add(boardModel);
    }

    public boolean removeBoard(BoardModel boardModel) {
        boardModel.removeParticipant(this);
        return boards.remove(boardModel);
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
