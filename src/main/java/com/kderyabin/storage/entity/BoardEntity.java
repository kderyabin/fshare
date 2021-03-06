package com.kderyabin.storage.entity;

import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Table(name = "board")
@NamedNativeQuery(
        name="BoardEntity.loadRecent",
        query = "select b.* from board b order by b.update desc limit ?1",
        resultClass = BoardEntity.class
)
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "currency", length = 3)
    private String currency;

    @Column(name = "creation", nullable = false)
    private Timestamp creation = new Timestamp(System.currentTimeMillis());

    @Column(name = "update", nullable = false)
    private Timestamp update = new Timestamp(System.currentTimeMillis());

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable( name="board_person",
                joinColumns = { @JoinColumn( name = "boardId")},
                inverseJoinColumns = { @JoinColumn( name = "personId")})
    private Set<PersonEntity> participants = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "board", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<BoardItemEntity> items = new LinkedHashSet<>();

    public BoardEntity() {
    }

    public BoardEntity(String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreation() {
        return creation;
    }

    public void setCreation(Timestamp creation) {
        this.creation = creation;
    }

    public Timestamp getUpdate() {
        return update;
    }

    public void setUpdate(Timestamp update) {
        this.update = update;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<PersonEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<PersonEntity> participants) {
        this.participants = participants;
    }

    public boolean addParticipant(PersonEntity participant){
        //participant.addBoard(this);
        return participants.add(participant);
    }
    public boolean removeParticipant(PersonEntity participant){
        //participant.removeBoard(this);
        return participants.remove(participant);
    }

    public Set<BoardItemEntity> getItems() {
        return items;
    }

    public void setItems(Set<BoardItemEntity> items) {
        this.items = items;
    }

    public void addItem(BoardItemEntity item){
        item.setBoard(this);
        items.add(item);
    }

    public void removeItem(BoardItemEntity item){
        item.setBoard(null);
        items.remove(item);
    }
    public void removeAllItems(){
        if(null == items) {
            return;
        }
        items.forEach(item -> {
            item.setBoard(null);
            item.setPerson(null);
        });
        items.clear();
    }

    /**
     *
     * @return Current timestamp.
     */
    private Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Reset update field to current timestamp.
     */
    public void initUpdateTime(){
        update = getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardEntity that = (BoardEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creation, that.creation) &&
                Objects.equals(update, that.update) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creation, update);
    }

}
