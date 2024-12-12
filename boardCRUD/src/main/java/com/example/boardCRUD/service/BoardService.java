package com.example.boardCRUD.service;

import com.example.boardCRUD.dto.BoardDTO;
import com.example.boardCRUD.entity.BoardEntity;
import com.example.boardCRUD.mapper.BoardMapper;
import com.example.boardCRUD.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public void save(BoardDTO boardDTO) throws IOException {
        if(boardDTO.getBoardFile().isEmpty()){
            // 첨부 파일 없음
            BoardEntity boardEntity = boardMapper.toEntity(boardDTO);
            boardRepository.save(boardEntity);
        }else{
            // 첨부 파일 있음
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            String savePath = "C:\\Users\\jjm00\\Desktop\\개발\\Temp" + storedFileName;
            boardFile.transferTo(new File(savePath));
        }

    }

    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(boardMapper.toDto(boardEntity));
        }

        return boardDTOList;

        /* 람다식을 사용한 조회 방법
        List<BoardDTO> boardDTOList = boardEntityList.stream()
                .map(boardMapper::toDto)
                .collect(Collectors.toList());
         */

    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> boardEntityOptional = boardRepository.findById(id);
        if (boardEntityOptional.isPresent()) {
            BoardEntity boardEntity = boardEntityOptional.get();
            return boardMapper.toDto(boardEntity);
        }
        return null;
    }

    public BoardDTO update(BoardDTO boardDTO) {
        // 기존 Entity 조회
        BoardEntity existingBoard = boardRepository.findById(boardDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        // Title, Contents, fileAttached 필드만 수정
        existingBoard.setBoardTitle(boardDTO.getBoardTitle());
        existingBoard.setBoardContents(boardDTO.getBoardContents());
        existingBoard.setFileAttached(boardDTO.getFileAttached());

        // 수정된 Entity 저장
        boardRepository.save(existingBoard);

        return boardMapper.toDto(existingBoard);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 5;
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.ASC, "id")));

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

        return boardDTOS;
    }
}
