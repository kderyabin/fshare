package com.kderyabin.storage.entity;

import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Table(name = "person")
public class PersonEntity {
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
    private Set<BoardEntity> boards = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "person", cascade =  {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<BoardItemEntity> items = new LinkedHashSet<>();

    public PersonEntity() {
    }

    public PersonEntity(String name) {
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

    public Set<BoardEntity> getBoards() {
        return boards;
    }

    public void setBoards(Set<BoardEntity> boards) {
        this.boards = boards;
    }

    public boolean addBoard(BoardEntity boardModel) {
        return boards.add(boardModel);
    }

    public boolean removeBoard(BoardEntity boardModel) {
        return boards.remove(boardModel);
    }

    public Set<BoardItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<BoardItemEntity> items) {
        this.items = items;
    }

    public void addItem(BoardItemEntity item){
        items.add(item);
    }
    public void removeItem(BoardItemEntity item){
        items.remove(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEntity that = (PersonEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
