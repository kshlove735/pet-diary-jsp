package com.myproject.petcare.pet_diary.diary.repository;

import com.myproject.petcare.pet_diary.diary.entity.Diary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiaryRepositoryCustom {
    Page<Diary> findByPetIdAndDtype(List<Long> petIds, List<String> dtype, Pageable pageable);
}
