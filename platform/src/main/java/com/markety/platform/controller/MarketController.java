package com.markety.platform.controller;


import com.markety.platform.dto.BoardDto;
import com.markety.platform.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {

    private final BoardService boardService;

    @Value("${board-attach-dir}")
    private String boardAttachDir;

    @GetMapping("detail/{id}")
    public String getProductDetail(@PathVariable long id, Model model) {
        BoardDto product = boardService.getProductDetailById(id);
        model.addAttribute("product", product);
        model.addAttribute("attachDir", boardAttachDir);

        return "market/detail";
    }

    @GetMapping("/form")
    public String showMarketForm() {
        return "market/form";
    }

    @GetMapping("/list")
    public String showMarketList() {
        return "market/list";
    }

}
