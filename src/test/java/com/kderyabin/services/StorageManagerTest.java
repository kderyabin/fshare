package com.kderyabin.services;

import com.kderyabin.model.BoardModel;
import com.kderyabin.model.PersonModel;
import com.kderyabin.model.SettingModel;
import com.kderyabin.storage.repository.SettingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestEntityManager
class StorageManagerTest {
    final private Logger LOG = LoggerFactory.getLogger(StorageManagerTest.class);

    @Autowired
    StorageManager storageManager;

    @Autowired
    SettingRepository settingRepository;

    @Test
    void savePersonModel() {
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
        model.setCurrency("EUR");
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
        PersonModel newPerson = result.getParticipants().stream().filter(p -> p.getName().equals("Anna")).findFirst().get();
        // Checks new person is added into DB (ID is generated)
        assertNotNull(newPerson.getId());
        Integer id = person1.getId();
        // Check existing participant is not registered in DB (ID is not regenerated in our case)
        PersonModel registeredPerson = result.getParticipants().stream().filter(p -> p.getId().equals(id)).findFirst().get();
        assertNotNull(registeredPerson);
    }

    @Test
    void saveSettings() {
        settingRepository.deleteAll();
        // prepare data
        SettingModel currency = new SettingModel("currency", "EUR");
        SettingModel language = new SettingModel("lang", "fr");

        List<SettingModel> settings = new ArrayList<>(
                Arrays.asList(currency, language)
        );

        // Test insertion
        List<SettingModel> result = storageManager.save(settings);

        assertEquals(2, result.size());
        // Currency must have id set up.
        SettingModel currencySaved =  result
                .stream()
                .filter( s -> s.getName().equals(currency.getName()))
                .findFirst()
                .orElse(null);
        assertNotNull(currencySaved);
        assertNotNull(currencySaved.getId());
        SettingModel languageSaved =  result
                .stream()
                .filter( s -> s.getName().equals(language.getName()))
                .findFirst()
                .orElse(null);
        assertNotNull(languageSaved);
        assertNotNull(languageSaved.getId());

        // Tests update
        currencySaved.setValue("USD");
        languageSaved.setValue("en");

        List<SettingModel> settingsUpdate = new ArrayList<>(
                Arrays.asList(currencySaved, languageSaved)
        );
        result = storageManager.save(settingsUpdate);

        result.forEach( s -> {
            switch (s.getName()) {
                case "currency" :
                    assertEquals(currencySaved.getValue(), s.getValue());
                     break;
                case "lang":
                    assertEquals(languageSaved.getValue(), s.getValue());
                    break;
            }
        });
    }

    @Test
    void getSettings() {
        settingRepository.deleteAll();

        SettingModel currency = new SettingModel("currency", "EUR");
        SettingModel language = new SettingModel("lang", "fr");

        List<SettingModel> settings = new ArrayList<>(
                Arrays.asList(currency, language)
        );

        storageManager.save(settings);

        List<SettingModel> result = storageManager.getSettings();
        assertEquals(2, result.size());
    }

}
