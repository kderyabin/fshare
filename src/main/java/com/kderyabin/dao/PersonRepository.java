package com.kderyabin.dao;

import com.kderyabin.models.PersonModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<PersonModel, Integer> {
}
