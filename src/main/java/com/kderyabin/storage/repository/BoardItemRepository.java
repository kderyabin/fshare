package com.kderyabin.storage.repository;

import com.kderyabin.storage.entity.BoardItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardItemRepository extends JpaRepository<BoardItemEntity, Integer> {
    List<BoardItemEntity> findAllByBoardId(Integer boardId);
}
