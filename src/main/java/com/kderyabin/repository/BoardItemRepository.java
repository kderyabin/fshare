package com.kderyabin.repository;

import com.kderyabin.models.BoardItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemModel, Integer> {
    List<BoardItemModel> findAllByBoardId(Integer boardId);
}
