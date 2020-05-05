package com.kderyabin.storage.repository;

import com.kderyabin.storage.entity.BoardItemEntity;
import com.kderyabin.storage.entity.BoardEntity;
import com.kderyabin.storage.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        PersonEntity person1 = new PersonEntity("John");
        person1 = personRepository.save(person1);
        PersonEntity person2 = new PersonEntity("Anna");
        person2 = personRepository.save(person2);

        BoardEntity board = new BoardEntity("Board 1");
        board.addParticipant(person1);
        board.addParticipant(person2);
        repository.save(board);

        List<BoardEntity> list = repository.findAll();
        assertEquals(1, list.size());

        List<PersonEntity> persons = new ArrayList<>(list.get(0).getParticipants());
        assertEquals(2, persons.size());
    }

    private Integer prepareRemovalTestData(){
        // Prepare initial data
        PersonEntity person = new PersonEntity("Jeremy");
        person = personRepository.save(person);

        BoardEntity board = new BoardEntity("Board 1");
        person.getBoards().add(board);
        board.addParticipant(person);
        repository.saveAndFlush(board);

        BoardItemEntity model = new BoardItemEntity("lunch");
        model.setAmount("10");
        model.setBoard(board);
        model.setPerson(person);
        itemRepository.saveAndFlush(model);

        BoardItemEntity model2 = new BoardItemEntity("dinner");
        model2.setBoard(board);
        model2.setAmount("19.90");
        model2.setPerson(person);
        itemRepository.saveAndFlush(model2);

        return board.getId();
    }
    @Transactional
    @Test
    public void removeBoardWithItems() {
        Integer boardId = prepareRemovalTestData();
        // Load board from the DB
        BoardEntity object = repository.findById(boardId).orElse(null);

        LOG.info("Deleting");
        repository.delete(object);

        LOG.info("Checking rows in DB");
        List<BoardItemEntity> items = itemRepository.findAllByBoardId(boardId);
        assertEquals(0, items.size());
    }

    /**
     * Prepare test data in separate method
     * @return
     */
    private BoardEntity prepareLoadRecent() {
        // Prepare data
        PersonEntity person1 = new PersonEntity("Jeremy");
        person1 = personRepository.save(person1);
        PersonEntity person2 = new PersonEntity("Sam");
        person2 = personRepository.save(person2);

        BoardEntity board1 = new BoardEntity("Board 1");
        board1.addParticipant(person1);
        board1.addParticipant(person2);
        // 01 apr 2020 00:00:01
        board1.setCreation(new Timestamp(1585692001000L));
        board1.setUpdate(new Timestamp(1585692001000L));
        repository.save(board1);

        BoardEntity board2 = new BoardEntity("Board 2");
        board2.addParticipant(person1);
        board2.addParticipant(person2);
        // 24 apr 2020 00:00:01
        board2.setCreation(new Timestamp(1587679201000L));
        board2.setUpdate(new Timestamp(1587679201000L));

        board2 = repository.save(board2);

        return board2;
    }

    /**
     * Fetch test results in separate method
     * @return
     */
    private List<BoardEntity> prepareLoadRecentGetResults() {
         return repository.loadRecent(1);
    }

    /**
     * Test loading in separate methods in the scope of method Transaction
     */
    @Transactional
    @Test
    public void loadRecent() {
        BoardEntity board2 = prepareLoadRecent();
        // Test
        List<BoardEntity> result = prepareLoadRecentGetResults();

        assertEquals(1, result.size());
        assertEquals(board2, result.get(0));
    }
}
