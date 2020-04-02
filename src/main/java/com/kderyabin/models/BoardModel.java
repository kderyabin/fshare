package com.kderyabin.models;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "board")
public class BoardModel {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation", nullable = false)
    private LocalDateTime creation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update", nullable = false)
    private LocalDateTime update;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PersonModel> participants = new ArrayList<>();

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

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public LocalDateTime getUpdate() {
        return update;
    }

    public void setUpdate(LocalDateTime update) {
        this.update = update;
    }

    public List<PersonModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<PersonModel> participants) {
        this.participants = participants;
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
