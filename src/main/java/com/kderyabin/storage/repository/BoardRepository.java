package com.kderyabin.storage.repository;

import com.kderyabin.storage.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
    List<BoardEntity> loadRecent(Integer limit);
}
