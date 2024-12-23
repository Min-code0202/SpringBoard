package com.example.boardCRUD.service;

import com.example.boardCRUD.dto.BoardDTO;
import com.example.boardCRUD.dto.CommentDTO;
import com.example.boardCRUD.entity.BoardEntity;
import com.example.boardCRUD.entity.CommentEntity;
import com.example.boardCRUD.mapper.CommentMapper;
import com.example.boardCRUD.repository.BoardRepository;
import com.example.boardCRUD.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentMapper commentMapper;

    public Long save(CommentDTO commentDTO) {
        /* 부모엔티티(BoardEntity) 조회 */
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = commentMapper.toEntity(commentDTO);
            commentEntity.setBoardEntity(boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else{
            return null;
        }
    }

    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = commentMapper.toDto(commentEntity);
            commentDTO.setBoardId(boardId);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }
}
