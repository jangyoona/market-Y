package com.markety.platform.mapper;

import com.markety.platform.dto.BoardAttachDto;
import com.markety.platform.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    void insertBoard(BoardDto board);

    void insertAttach(BoardAttachDto attach);
}
