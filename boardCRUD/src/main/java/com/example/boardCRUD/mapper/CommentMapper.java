package com.example.boardCRUD.mapper;

import com.example.boardCRUD.dto.CommentDTO;
import com.example.boardCRUD.entity.CommentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "createdTime", target = "commentCreatedTime")
    CommentDTO toDto(CommentEntity commentEntity);
    @Mapping(target = "boardEntity", ignore = true) // 추가 데이터 수동 설정
    CommentEntity toEntity(CommentDTO commentDTO);
}
