package com.kderyabin.storage.repository;

import com.kderyabin.model.BoardPersonTotal;
import com.kderyabin.model.IBoardPersonTotal;
import com.kderyabin.storage.entity.BoardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Integer> {
    List<BoardItemEntity> findAllByBoardId(Integer boardId);

    List<Object[]> getBoardPersonTotal(Integer boardId);
}
