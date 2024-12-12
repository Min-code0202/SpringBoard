package com.example.boardCRUD.mapper;

import com.example.boardCRUD.dto.BoardDTO;
import com.example.boardCRUD.entity.BoardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    @Mapping(source = "createdTime", target = "boardCreatedTime")
    @Mapping(source = "updatedTime", target = "boardUpdatedTime")
    BoardDTO toDto(BoardEntity boardEntity);
    @Mapping(target="boardHits", expression = "java(0)")
    @Mapping(target="fileAttached", expression = "java(0)")
    BoardEntity toEntity(BoardDTO boardDTO);
}
