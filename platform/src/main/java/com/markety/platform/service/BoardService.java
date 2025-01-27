package com.markety.platform.service;

import com.markety.platform.dto.BoardAttachDto;
import com.markety.platform.dto.BoardDto;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    List<BoardDto> getAllProduct();
    void addProduct(BoardDto board, MultipartFile[] attachs);
    void editBoard(BoardDto board, List<BoardAttachDto> attachs);

    BoardDto getProductDetailById(long id);
}
