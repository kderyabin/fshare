package com.kderyabin.dao;

import com.kderyabin.models.BoardModel;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepository extends CrudRepository<BoardModel, Integer> {
}
