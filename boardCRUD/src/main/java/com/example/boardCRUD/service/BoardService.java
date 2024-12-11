package com.example.boardCRUD.service;

import com.example.boardCRUD.dto.BoardDTO;
import com.example.boardCRUD.entity.BoardEntity;
import com.example.boardCRUD.mapper.BoardMapper;
import com.example.boardCRUD.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = boardMapper.toEntity(boardDTO);
        boardRepository.save(boardEntity);
    }
}
