package com.markety.platform.service;

import com.markety.platform.common.UserUtil;
import com.markety.platform.common.Util;
import com.markety.platform.config.security.WebUserDetails;
import com.markety.platform.dto.BoardAttachDto;
import com.markety.platform.dto.BoardDto;
import com.markety.platform.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;

    @Value("${board-attach-dir}")
    private String boardAttachDir;

    @Override
    public BoardDto getProductDetailById(long id) {
        return boardMapper.findProductById(id);
    }

    @Override
    public List<BoardDto> getAllProduct() {
        return boardMapper.findAllProduct();
    }


    @Transactional(rollbackFor = Exception.class,
            propagation = Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED)
    @Override
    public void addProduct(BoardDto board, MultipartFile[] attachs) {

        WebUserDetails user = UserUtil.getUserDetails();
        board.setWriter(user.getUser().getId());

        boardMapper.insertBoard(board);

        // 첨부 파일이 존재하는 경우에만 처리
        if (attachs != null && attachs.length > 0) {
            try {
                for (MultipartFile file : attachs) {
                    String originName = file.getOriginalFilename();
                    String savedName = Util.makeUniqueFileName(originName);

                    createDirectoryIfNotExists();

                    // 파일 저장
                    file.transferTo(new File(boardAttachDir, savedName));

                    // DB 저장
                    BoardAttachDto attach = new BoardAttachDto();
                    // useGeneratedKeys 설정 시 `insert` 실행에 사용한 객체의 파라미터에 설정한 id 값이 셋팅된다. *참고로 map 객체도 받을 수 있음
                    attach.setBoardId(board.getId());
                    attach.setOriginName(originName);
                    attach.setSavedName(savedName);

                    boardMapper.insertAttach(attach);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void editBoard(BoardDto board, List<BoardAttachDto> attachs) {

//        boardMapper.updateBoard(board);
//
//        // 첨부 파일이 존재하는 경우에만 처리
//        if (attachs != null && !attachs.isEmpty()) {
//            for (BoardAttachDto attach : attachs) {
//                attach.setBoardId(board.getId());
//                boardMapper.updateAttach(attach);
//            }
//        }
    }

    /**
     * 파일 디렉터리 없으면 생성
     **/
    private void createDirectoryIfNotExists() {
        File uploadDirectory = new File(boardAttachDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs(); // 디렉토리가 없으면 생성
        }
    }

}
