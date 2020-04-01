package com.kderyabin.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime creation;
    private LocalDateTime update;
    private List<PersonModel> participants = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
