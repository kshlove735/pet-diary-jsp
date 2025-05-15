package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DiaryViewController {

    private final DiaryService diaryService;

    @GetMapping("/diary")
    public String createPetPage(Model model) {
        //model.addAttribute("partialPetReqDto", new PartialPetReqDto());
        return "diaryList";
    }

}
