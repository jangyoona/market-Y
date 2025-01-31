package com.markety.platform.controller.api;

import com.markety.platform.dto.BoardAttachDto;
import com.markety.platform.dto.BoardDto;
import com.markety.platform.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketApi {

    private final BoardService boardService;

    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpStatus> addProduct(BoardDto board, MultipartFile[] attachs) {
        System.out.println(board.toString());

        try {
            boardService.addProduct(board, attachs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PutMapping("/edit")
    public ResponseEntity<HttpStatus> editBoard(BoardDto board, List<BoardAttachDto> attachs) {
        System.out.println(board.toString());

        try {
            boardService.editBoard(board, attachs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(HttpStatus.CREATED);

    }


}
