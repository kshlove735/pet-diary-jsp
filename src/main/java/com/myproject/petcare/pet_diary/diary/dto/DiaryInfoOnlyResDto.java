package com.myproject.petcare.pet_diary.diary.dto;

import com.myproject.petcare.pet_diary.diary.entity.Diary;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryInfoOnlyResDto {

    private Long diaryId;
    private Long petId;
    private LocalDate date;
    private String dtype;
    private String description;

    public DiaryInfoOnlyResDto(Diary diary) {
        this.diaryId = diary.getId();
        this.petId = diary.getPet().getId();
        this.date = diary.getDate();
        this.dtype = diary.getDtype();
        this.description = diary.getDescription();
    }
}
