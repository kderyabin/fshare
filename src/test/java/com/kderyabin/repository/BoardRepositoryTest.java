package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    //@Transactional
    @Test
    public void removeBoardWithItems() {
        // Prepare initial data
        PersonModel person = new PersonModel("Jeremy");
        LOG.info("init person");
        BoardModel board = new BoardModel("Board 1");
        LOG.info("init board");
        board.addParticipant(person);
        LOG.info("add person to board");

        repository.save(board);
        LOG.info("Saved board");

        BoardItemModel model = new BoardItemModel("lunch");
        model.setAmount("10");
        model.setBoard(board);
        model.setPerson(person);
        LOG.info("Start saving item 1");
        itemRepository.save(model);
        LOG.info("saved");
        BoardItemModel model2 = new BoardItemModel("dinner");
        model2.setBoard(board);
        model2.setAmount("19.90");
        model2.setPerson(person);
        LOG.info("Start saving item 2");
        itemRepository.save(model2);
        LOG.info("saved");

        // Test start here
        Integer boardId = board.getId();
        // Load board from the DB
        LOG.info("Load board from the DB");
        BoardModel object = repository.findById(boardId).orElse(null);
        repository.delete(object);
        LOG.info("Board removed");
        LOG.info("Start fetching items");
        List<BoardItemModel> items = (List<BoardItemModel>) itemRepository.findAllByBoardId(boardId);
        LOG.info("End fetching items");
        assertEquals(0, items.size());
    }
}
