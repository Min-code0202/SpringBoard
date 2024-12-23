package com.example.boardCRUD.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private MultipartFile boardFile; // 파일 담는 용도
    private String originalFileName; // 원본 파일명
    private String storedFileName; // 서버 저장용 파일명
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    private int youtubeAttached; // 유튜브 링크 여부
    private String youtubeLink; // 재생할 유튜브 영상 링크

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }
}
