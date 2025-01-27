package com.markety.platform.controller;

import com.markety.platform.dto.BoardDto;
import com.markety.platform.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @Value("${board-attach-dir}")
    private String boardAttachDir;

    private final BoardService boardService;

    @RequestMapping(path = {"/", "/home"})
    public String home(Model model) {

        List<BoardDto> productList = boardService.getAllProduct();
        model.addAttribute("productList", productList);
        model.addAttribute("attachDir", boardAttachDir);

        return "index";
    }
}
