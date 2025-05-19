package com.myproject.petcare.pet_diary.diary.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.diary.dto.*;
import com.myproject.petcare.pet_diary.diary.service.DiaryService;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/diary")
    public ResponseDto<Page<DiaryInfoWithJoinResDto>> getDiaryList(
            @RequestParam(value = "petId", required = false) List<Long> petIds,
            @RequestParam(value = "dtype", required = false) List<String> dtypes,
            Pageable pageable
    ) {

        Page<DiaryInfoWithJoinResDto> diaryInfoResDtos = diaryService.getDiaryList(petIds, dtypes, pageable);
        return new ResponseDto<>(true, "일기 리스트 조회 성공", diaryInfoResDtos);
    }

    @GetMapping("/diary/{diaryId}")
    public ResponseDto getDiary(
            @PathVariable("diaryId") @Positive(message = "일기 ID는 양수여야 합니다.") Long diaryId
    ) {
        DiaryInfoWithJoinResDto diaryinfo = diaryService.getDiaryById(diaryId);
        return new ResponseDto<>(true, "일기 단일 조회 성공", diaryinfo);
    }

    @PostMapping("/diary/{petId}")
    public ResponseDto createDiary(
            @PathVariable("petId") @Positive(message = "반려견 ID는 양수여야 합니다.") Long petId,
            @RequestBody @Validated DiaryReqDto diaryReqDto
    ) {

        ResponseDto diaryInfoResDto = diaryService.createDiary(petId, diaryReqDto);
        return diaryInfoResDto;
    }

    @PutMapping("/diary/{diaryId}")
    public ResponseDto updateDiary(
            @PathVariable("diaryId") @Positive(message = "일기 ID는 양수여야 합니다.") Long diaryId,
            @RequestBody @Validated DiaryReqDto diaryReqDto
    ) {
        ResponseDto diaryInfoResDto = diaryService.updateDiary(diaryId, diaryReqDto);
        return diaryInfoResDto;
    }

    @DeleteMapping("/diary/{diaryId}")
    public ResponseDto deleteDiary(
            @PathVariable("diaryId") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        diaryService.deleteDiary(diaryId, customUserDetails);
        return new ResponseDto<>(true, "일기 삭제 성공", null);
    }
}
