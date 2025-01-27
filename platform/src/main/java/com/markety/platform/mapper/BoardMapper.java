package com.markety.platform.mapper;

import com.markety.platform.dto.BoardAttachDto;
import com.markety.platform.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    void insertBoard(BoardDto board);

    void insertAttach(BoardAttachDto attach);

    List<BoardDto> findAllProduct();

    BoardDto findProductById(long id);
}
