package com.kderyabin.dao;

import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureTestEntityManager
class BoardRepositoryTest {

    @Autowired
    private BoardRepository repository;
    @Autowired
    private PersonRepository personRepository;


    @BeforeAll
    public void setUp(){
        PersonModel person1 = new PersonModel("John");
        PersonModel person2 = new PersonModel("Anna");

        BoardModel board = new BoardModel("Board 1");
        board.addParticipant(person1);
        board.addParticipant(person2);

        repository.save(board);
    }

    @Transactional
    @Test
    public void save(){
        List<BoardModel> list =  (List<BoardModel>) repository.findAll();
        assertEquals(1, list.size());

        BoardModel found = list.get(0);

        List<PersonModel> persons =  new ArrayList<PersonModel>(found.getParticipants());

        assertEquals(2, persons.size());
        System.out.println(persons);
    }
}
