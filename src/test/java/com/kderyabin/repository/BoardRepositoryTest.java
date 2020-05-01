package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
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

    PersonModel personJack ;

    @Transactional
    @BeforeEach
    void setUp() {

        personJack = new PersonModel("Jeremy");
        personJack = personRepository.save(personJack);
        LOG.info(">>>>>>>>>>>> END setUp");
    }

    @Transactional
    @Test
    public void findAll() {

        PersonModel person1 = new PersonModel("John");
        PersonModel person2 = new PersonModel("Anna");

        BoardModel board = new BoardModel("Board 1");
        board.addParticipant(person1);
        board.addParticipant(person2);
        repository.save(board);

        List<BoardModel> list = repository.findAll();
        assertEquals(1, list.size());

        List<PersonModel> persons = new ArrayList<>(list.get(0).getParticipants());
        assertEquals(2, persons.size());
    }

    private Integer prepareRemovalTestData(){
        // Prepare initial data
        PersonModel person = new PersonModel("Jeremy");
        //person = personRepository.save(person);
        BoardModel board = new BoardModel("Board 1");
        person.getBoards().add(board);
        board.addParticipant(person);
        repository.saveAndFlush(board);

        BoardItemModel model = new BoardItemModel("lunch");
        model.setAmount("10");
        model.setBoard(board);
        model.setPerson(person);
        itemRepository.saveAndFlush(model);

        BoardItemModel model2 = new BoardItemModel("dinner");
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
        BoardModel object = repository.findById(boardId).orElse(null);

        LOG.info("Deleting");
        repository.delete(object);

        LOG.info("Checking rows in DB");
        List<BoardItemModel> items = itemRepository.findAllByBoardId(boardId);
        assertEquals(0, items.size());
    }

    /**
     * Prepare test data in separate method
     * @return
     */
    private BoardModel prepareLoadRecent() {
        // Prepare data
        PersonModel person1 = new PersonModel("Jeremy");
        person1 = personRepository.save(person1);
        PersonModel person2 = new PersonModel("Sam");

        BoardModel board1 = new BoardModel("Board 1");
        board1.addParticipant(personJack);
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

        board2 = repository.save(board2);

        return board2;
    }

    /**
     * Fetch test results in separate method
     * @return
     */
    private List<BoardModel> prepareLoadRecentGetResults() {
         return repository.loadRecent(1);
    }

    /**
     * Test loading in separate methods in the scope of method Transaction
     */
    @Transactional
    @Test
    public void loadRecent() {
        BoardModel board2 = prepareLoadRecent();
        // Test
        List<BoardModel> result = prepareLoadRecentGetResults();

        assertEquals(1, result.size());
        assertTrue(board2.equals(result.get(0)));
    }
}
