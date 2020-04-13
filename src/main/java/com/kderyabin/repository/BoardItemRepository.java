package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardItemRepository extends CrudRepository<BoardItemModel, Integer> {
    Iterable<BoardItemModel> findAllByBoardId(Integer boardId);
}
