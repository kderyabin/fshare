package com.kderyabin.storage.entity;

import javax.persistence.Table;

@Table(name = "board_person")
public class BoardPersonEntity {
    Integer boardId;
    Integer personId;
}
