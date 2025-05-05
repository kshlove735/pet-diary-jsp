package com.myproject.petcare.pet_diary.user.dto;

import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserInfoResDto {

    private String email;
    private String name;
    private String phone;
    private List<PetInfoResDto> petInfos = new ArrayList();

    public UserInfoResDto(String email, String name, String phone) {
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public UserInfoResDto(String email, String name, String phone, List<Pet> pets) {
        this.email = email;
        this.name = name;
        this.phone = phone;

        for (Pet pet : pets) {
            PetInfoResDto petInfoResDto = new PetInfoResDto(
                    pet.getId(), pet.getName(), pet.getBreed(), pet.getBirthDate(),
                    pet.getGender(), pet.getWeight(), pet.getDescription(), pet.getCreateDate(), pet.getUpdatedDate());

            petInfos.add(petInfoResDto);
        }
    }
}
