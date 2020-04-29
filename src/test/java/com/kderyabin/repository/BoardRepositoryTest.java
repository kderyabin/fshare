package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestEntityManager
class BoardRepositoryTest {

    final private Logger LOG = LoggerFactory.getLogger(BoardRepositoryTest.class);

    @Autowired
    private BoardRepository repository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BoardItemRepository itemRepository;

    @Transactional
    @Test
    public void findAll() {
        PersonModel person1 = new PersonModel("John");
        PersonModel person2 = new PersonModel("Anna");

        BoardModel board = new BoardModel("Board 1");
        board.addParticipant(person1);
        board.addParticipant(person2);

        repository.save(board);

        List<BoardModel> list = (List<BoardModel>) repository.findAll();
        assertEquals(1, list.size());

        BoardModel found = list.get(0);

        List<PersonModel> persons = new ArrayList<>(found.getParticipants());

        assertEquals(2, persons.size());
    }

    @Test
    public void removeBoardWithItems() {
        // Prepare initial data
        PersonModel person = new PersonModel("Jeremy");
        //person = personRepository.save(person);
        BoardModel board = new BoardModel("Board 1");
        board.addParticipant(person);
        repository.save(board);

        BoardItemModel model = new BoardItemModel("lunch");
        model.setAmount("10");
        model.setBoard(board);
        model.setPerson(person);
        itemRepository.save(model);

        BoardItemModel model2 = new BoardItemModel("dinner");
        model2.setBoard(board);
        model2.setAmount("19.90");
        model2.setPerson(person);
        itemRepository.save(model2);

        // Test start here
        Integer boardId = board.getId();
        // Load board from the DB
        LOG.info("Load board from the DB");
        BoardModel object = repository.findById(boardId).orElse(null);
        repository.delete(object);
        List<BoardItemModel> items = (List<BoardItemModel>) itemRepository.findAllByBoardId(boardId);
        assertEquals(0, items.size());
    }
    @Transactional
    @Test
    void loadRecent() {
        // Prepare data
        PersonModel person1 = new PersonModel("Jeremy");
        //person1 = personRepository.save(person1);
        PersonModel person2 = new PersonModel("Sam");
        //person2 = personRepository.save(person2);

        BoardModel board1 = new BoardModel("Board 1");
        board1.addParticipant(person1);
        board1.addParticipant(person2);
        // 01 apr 2020 00:00:01
        board1.setCreation(new Timestamp(1585692001000L));
        board1.setUpdate(new Timestamp(1585692001000L));
        repository.save(board1);

        BoardModel board2 = new BoardModel("Board 2");
        board2.addParticipant(person1);
        board2.addParticipant(person2);
        // 24 apr 2020 00:00:01
        board2.setCreation(new Timestamp(1587679201000L));
        board2.setUpdate(new Timestamp(1587679201000L));

        repository.save(board2);

        // Test
        List<BoardModel> result = repository.loadRecent(1);

        assertEquals(1, result.size());
        assertTrue(board2.equals(result.get(0)));
    }
}
