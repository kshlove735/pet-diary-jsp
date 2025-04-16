package com.myproject.petcare.pet_diary.pet.repository;

import com.myproject.petcare.pet_diary.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
