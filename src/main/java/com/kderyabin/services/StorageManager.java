package com.kderyabin.services;

import com.kderyabin.model.BoardItemModel;
import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import com.kderyabin.storage.entity.BoardEntity;
import com.kderyabin.storage.entity.BoardItemEntity;
import com.kderyabin.storage.entity.PersonEntity;
import com.kderyabin.storage.repository.BoardItemRepository;
import com.kderyabin.storage.repository.BoardRepository;
import com.kderyabin.storage.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageManager {

    final private Logger LOG = LoggerFactory.getLogger(StorageManager.class);

    BoardRepository boardRepository;
    PersonRepository personRepository;
    BoardItemRepository itemRepository;

    private boolean lazyMode = true;

    public List<BoardModel> getRecentBoards(int limit) {
        return boardRepository.loadRecent(limit)
                .stream().map(this::getModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<BoardModel> getBoards() {
        List<BoardModel> result = new LinkedList<>();
        boardRepository.findAll().forEach(
                entity -> result.add(getModel(entity))
        );
        return result;
    }

    @Transactional
    public BoardModel loadParticipants(BoardModel model) {
        model.getParticipants().clear();
        personRepository.findAllByBoardId(model.getId())
                .forEach(e -> model.addParticipant(getModel(e)));

        return model;
    }

    @Transactional
    public List<PersonModel> getParticipants(BoardModel model) {
        List<PersonModel> result = new ArrayList<>();
        personRepository.findAllByBoardId(model.getId())
                .forEach(e -> result.add(getModel(e)));

        return result;
    }

    @Transactional
    public List<PersonModel> getPersons() {
        return personRepository.findAll().stream().map(this::getModel).collect(Collectors.toList());
    }

    @Transactional
    public PersonModel save(PersonModel model){
        LOG.info("Start PersonModel saving ");
        PersonEntity entity = getEntity(model);
        entity = personRepository.saveAndFlush(entity);
        model = getModel(entity);
        LOG.info("End PersonModel saving");
        return model;
    }

    @Transactional
    public BoardModel save(BoardModel model, boolean participants) {
        LOG.info("Start board saving ");
        boolean before = isLazyMode();
        setLazyMode(true);

        BoardEntity entity = getEntity(model);
        if (participants) {
            for (PersonModel person : model.getParticipants()) {
                // persist new entity
                PersonEntity pe = getEntity(person);
                if (pe.getId() == null) {
                    pe = personRepository.save(pe);
                    person.setId(pe.getId());
                }
                // add to the board
                entity.addParticipant(pe);
            }
        }

        entity.initUpdateTime();
        LOG.info("Entity: " + entity.toString());
        entity = boardRepository.saveAndFlush(entity);
        BoardModel result = getModel(entity);
        if(participants) {
            result.setParticipants(model.getParticipants());
        }
        LOG.info("Entity saved ");
        setLazyMode(before);
        return result;
    }

    public void save(BoardItemModel model) {
        BoardItemEntity entity = getEntity(model);
        BoardEntity board =  entity.getBoard();
        board.initUpdateTime();
        board = boardRepository.save(board);
        entity.setBoard(board);
        LOG.info(">>> Start BoardItemModel saving ");
        itemRepository.saveAndFlush(entity);
        LOG.info(">>> Saved BoardItemModel ");
        //return model;
    }

    public void removeBoard(BoardModel board) {
        boardRepository.deleteById(board.getId());
    }

    public BoardRepository getBoardRepository() {
        return boardRepository;
    }

    @Autowired
    public void setBoardRepository(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public PersonRepository getPersonRepository() {
        return personRepository;
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public BoardItemRepository getItemRepository() {
        return itemRepository;
    }

    @Autowired
    public void setItemRepository(BoardItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public boolean isLazyMode() {
        return lazyMode;
    }

    public void setLazyMode(boolean lazyMode) {
        this.lazyMode = lazyMode;
    }

    public BoardEntity getEntity(BoardModel model) {
        BoardEntity entity = new BoardEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setCreation(model.getCreation());
        entity.setUpdate(model.getUpdate());
//        if (!lazyMode) {
//            model.getParticipants().forEach(p -> entity.addParticipant(getEntity(p)));
//            model.getItems().forEach(i -> entity.addItem(getEntity(i)));
//        }

        return entity;
    }

    public BoardModel getModel(BoardEntity entity) {
        BoardModel model = new BoardModel();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setCreation(entity.getCreation());
        model.setUpdate(entity.getUpdate());
//        if (!lazyMode) {
//            entity.getParticipants().forEach(p -> model.addParticipant(getModel(p)));
//            entity.getItems().forEach(i -> model.addItem(getModel(i)));
//        }
        return model;
    }


    public PersonEntity getEntity(PersonModel model) {
        PersonEntity entity = new PersonEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        if(model.getBoards().size() > 0) {
            LOG.info("Boards found");
            model.getBoards().forEach(board -> entity.addBoard(getEntity(board)));
        }
//        if (!lazyMode) {
//            model.getBoards().forEach(board -> entity.addBoard(getEntity(board)));
//            model.getItems().forEach(item -> entity.addItem(getEntity(item)));
//        }
        return entity;
    }

    public PersonModel getModel(PersonEntity source) {
        PersonModel target = new PersonModel();
        target.setId(source.getId());
        target.setName(source.getName());

        return target;
    }

    public BoardItemEntity getEntity(BoardItemModel source) {
        BoardItemEntity target = new BoardItemEntity();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAmount(source.getAmount());
        target.setDate(source.getDate());
        target.setPerson(getEntity(source.getPerson()));
        target.setBoard(getEntity(source.getBoard()));
        return target;
    }

    public BoardItemModel getModel(BoardItemEntity source) {
        BoardItemModel target = new BoardItemModel();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setAmount(source.getAmount());
        target.setDate(source.getDate());
        target.setPerson(getModel(source.getPerson()));
        target.setBoard(getModel(source.getBoard()));
        return target;
    }

    @Transactional
    public BoardModel loadItems(BoardModel model) {
        model.setItems(
                itemRepository.findAllByBoardId(model.getId())
                        .stream()
                        .map(this::getModel)
                        .collect(Collectors.toList())
        );
        return model;
    }


}
