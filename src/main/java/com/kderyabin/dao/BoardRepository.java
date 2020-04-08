package com.kderyabin.dao;

import com.kderyabin.models.BoardModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<BoardModel, Integer> {
}
