package com.kderyabin.repository;

import com.kderyabin.models.BoardModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends CrudRepository<BoardModel, Integer> {
    List<BoardModel> loadRecent(Integer limit);
}
