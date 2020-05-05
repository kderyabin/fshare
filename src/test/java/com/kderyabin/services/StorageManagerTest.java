package com.kderyabin.services;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestEntityManager
class StorageManagerTest {
    final private Logger LOG = LoggerFactory.getLogger(StorageManagerTest.class);

    @Autowired
    StorageManager storageManager;

    @Test
    void savePersonModel(){
        PersonModel person = new PersonModel("Jack");
        PersonModel result = storageManager.save(person);
        assertNotNull(result.getId());
        assertEquals(person.getName(), result.getName());
    }
    @Test
    void saveBoardModel() {
        PersonModel person1 = new PersonModel("Jack");
        person1 = storageManager.save(person1);
        // new board
        BoardModel model = new BoardModel("India");
        // existing user
        model.addParticipant(person1);
        // new user
        PersonModel person2 = new PersonModel("Anna");
        model.addParticipant(person2);

        BoardModel result = storageManager.save(model, true);
        // Checks ID is added into a response model
        assertNotNull(result.getId());
        // Checks participants are appended to the response.
        assertEquals(2, result.getParticipants().size());
        PersonModel newPerson = result.getParticipants().stream().filter( p -> p.getName().equals("Anna")).findFirst().get();
        // Checks new person is added into DB (ID is generated)
        assertNotNull(newPerson.getId());
        Integer id = person1.getId();
        // Check existing participant is not registered in DB (ID is not regenerated in our case)
        PersonModel registeredPerson = result.getParticipants().stream().filter( p -> p.getId().equals(id)).findFirst().get();
        assertNotNull(registeredPerson);
    }
}
