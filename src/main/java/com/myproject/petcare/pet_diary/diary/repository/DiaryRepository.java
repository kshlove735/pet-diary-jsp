package com.myproject.petcare.pet_diary.diary.repository;

import com.myproject.petcare.pet_diary.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {

    //@Query("SELECT d FROM Diary d WHERE d.pet.id = :petId and TYPE(d) = :dtype")
    //Page<Diary> findByPetId(@Param("petId") Long petId, @Param("dtype") Class<? extends Diary> dtype, Pageable pageable);
}
