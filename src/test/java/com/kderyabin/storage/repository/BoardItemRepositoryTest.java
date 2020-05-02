package com.kderyabin.storage.repository;

import com.kderyabin.storage.entity.BoardItemEntity;
import com.kderyabin.storage.entity.BoardEntity;
import com.kderyabin.storage.entity.PersonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureTestEntityManager
class BoardItemRepositoryTest {


    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardItemRepository itemRepository;

    @Transactional
    @Test
    public void save(){
        PersonEntity person = new PersonEntity("Konstantin");
        BoardEntity board = new BoardEntity("Paris");
        board.addParticipant(person);

        boardRepository.save(board);

        BoardItemEntity itemModel = new BoardItemEntity("Gasoil");
        itemModel.setAmount("54.86");
        itemModel.setBoard(board);
        itemModel.setPerson(person);

        itemRepository.save(itemModel);
    }
}
