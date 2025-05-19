package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.diary.service.DiaryService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DiaryViewController {

    private final DiaryService diaryService;

    @GetMapping("/diary")
    public String getDiaryPage(Model model) {
        return "diaryList";
    }

    @GetMapping("/diary/new")
    public String createDiaryPage(Model model) {
        return "createDiary";
    }

    @GetMapping("/diary/{diaryId}")
    public String getDiaryInfoPage(
            @PathVariable("diaryId") @Positive(message = "일기 ID는 양수여야 합니다.") Long diaryId,
            Model model) {
        model.addAttribute("diaryId", diaryId);
        return "diaryInfo";
    }

}
