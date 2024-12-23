package com.example.boardCRUD.repository;

import com.example.boardCRUD.entity.BoardEntity;
import com.example.boardCRUD.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
}
