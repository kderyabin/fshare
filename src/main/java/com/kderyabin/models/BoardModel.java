package com.kderyabin.models;

import lombok.ToString;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@ToString
@Entity
@Table(name = "board")
public class BoardModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creation", nullable = false)
    private Timestamp creation = new Timestamp(System.currentTimeMillis());

    @Column(name = "update", nullable = false)
    private Timestamp update = new Timestamp(System.currentTimeMillis());;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable( name="board_person",
                joinColumns = { @JoinColumn( name = "boardId")},
                inverseJoinColumns = { @JoinColumn( name = "personId")})
    private Set<PersonModel> participants = new LinkedHashSet<>();

    public BoardModel() {
    }

    public BoardModel(String name) {
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

    public Set<PersonModel> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<PersonModel> participants) {
        this.participants = participants;
    }

    public boolean addParticipant(PersonModel participant){
//        participant.addBoard(this);
        return participants.add(participant);
    }
    public boolean removeParticipant(PersonModel participant){
//        participant.removeBoard(this);
        return participants.remove(participant);
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
        BoardModel that = (BoardModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(creation, that.creation) &&
                Objects.equals(update, that.update);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, creation, update);
    }

}
