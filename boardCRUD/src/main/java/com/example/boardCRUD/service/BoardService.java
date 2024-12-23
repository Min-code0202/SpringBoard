package com.example.boardCRUD.service;

import com.example.boardCRUD.dto.BoardDTO;
import com.example.boardCRUD.entity.BoardEntity;
import com.example.boardCRUD.entity.BoardFileEntity;
import com.example.boardCRUD.mapper.BoardMapper;
import com.example.boardCRUD.repository.BoardFileRepository;
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
    private final BoardFileRepository boardFileRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        /*
         * boardHits, fileAttached, youtubeAttached 필드는
         * 0으로 초기화된 상태로 매핑됨
         * */
        BoardEntity boardEntity = boardMapper.toEntity(boardDTO);

        // 유튜브 링크 처리
        if (boardDTO.getYoutubeLink() != null && !boardDTO.getYoutubeLink().isEmpty()){
            boardEntity.setYoutubeLink(convertToEmbedYoutubeLink(boardDTO.getYoutubeLink()));
            boardEntity.setYoutubeAttached(1);
        }

        // 첨부 파일 처리
        if(boardDTO.getBoardFile().isEmpty()){
            // 첨부파일 없음
            boardRepository.save(boardEntity);
        }else {
            // 첨부 파일 있음
            MultipartFile boardFile = boardDTO.getBoardFile();
            String originalFileName = boardFile.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;

            // 저장 경로 생성
            String savePath = "C:\\Users\\jjm00\\Desktop\\개발\\Temp\\" + storedFileName;
            boardFile.transferTo(new File(savePath));

            // 첨부 파일 관련 정보 설정
            boardEntity.setFileAttached(1);
            BoardEntity savedBoardEntity = boardRepository.save(boardEntity);

            // BoardFileEntity 생성 및 저장
            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(savedBoardEntity, originalFileName, storedFileName);
            boardFileRepository.save(boardFileEntity); // boardEntity와 연결된 파일을 저장
        }
    }

    public String convertToEmbedYoutubeLink(String youtubeLink) {
        if (youtubeLink != null && !youtubeLink.isEmpty()) {
            String videoId = youtubeLink.split("v=")[1].split("&")[0]; // 'v=' 뒤의 videoId 추출
            return "https://www.youtube.com/embed/" + videoId; // 임베드 URL 생성
        }
        return null; // 링크가 없으면 null 반환
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

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();

            // MapStruct로 기본 변환
            BoardDTO boardDTO = boardMapper.toDto(boardEntity);

            // 추가 데이터 설정
            if (boardEntity.getFileAttached() == 1) {
                // 파일이 첨부된 경우, 파일 정보를 수동으로 DTO에 설정
                BoardFileEntity fileEntity = boardEntity.getBoardFileEntity();
                boardDTO.setOriginalFileName(fileEntity.getOriginalFileName());
                boardDTO.setStoredFileName(fileEntity.getStoredFileName());
            }
            return boardDTO;
        }
        return null;
    }


    public BoardDTO update(BoardDTO boardDTO) {
        // 기존 Entity 조회
        BoardEntity existingBoard = boardRepository.findById(boardDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        // Title, Contents, fileAttached, youtubeAttached 필드만 수정
        existingBoard.setBoardTitle(boardDTO.getBoardTitle());
        existingBoard.setBoardContents(boardDTO.getBoardContents());
        existingBoard.setFileAttached(boardDTO.getFileAttached());
        existingBoard.setYoutubeAttached(boardDTO.getYoutubeAttached());

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
