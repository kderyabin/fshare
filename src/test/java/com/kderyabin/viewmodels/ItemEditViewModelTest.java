package com.kderyabin.viewmodels;

import com.kderyabin.models.BoardModel;
import com.kderyabin.models.PersonModel;
import com.kderyabin.scopes.BoardScope;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemEditViewModelTest {

    final private BoardScope scope = new BoardScope();

    @BeforeAll
    public void setUp(){
        // Init test data
        PersonModel person1 = new PersonModel("John");
        person1.setId(1);
        PersonModel person2 = new PersonModel("Anna");
        person2.setId(2);

        BoardModel board = new BoardModel("Board 1");
        board.setId(1);

        board.addParticipant(person1);
        board.addParticipant(person2);
        scope.setModel(board);
    }
    @Test
    void initialize() {
        ItemEditViewModel viewModel = new ItemEditViewModel();
        viewModel.setScope(scope);
        viewModel.initialize();
        assertEquals(2, viewModel.getParticipants().size());
    }

    @Test
    void goBack() {
    }

    @Test
    void canGoBack() {
    }

    @Test
    void save() {
    }
}
