package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
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
        PersonModel person = new PersonModel("Konstantin");
        BoardModel board = new BoardModel("Paris");
        board.addParticipant(person);

        boardRepository.save(board);

        BoardItemModel itemModel = new BoardItemModel("Gasoil");
        itemModel.setAmount("54.86");
        itemModel.setBoard(board);
        itemModel.setPerson(person);

        itemRepository.save(itemModel);
    }
}
