package com.myproject.petcare.pet_diary.pet.controller;

import com.myproject.petcare.pet_diary.pet.dto.PartialPetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PetViewController {

    private final PetService petService;

    @GetMapping("/pet")
    public String createPetPage(Model model) {
        model.addAttribute("partialPetReqDto", new PartialPetReqDto());
        return "createPet";
    }

    @GetMapping("/pet/{petId}")
    public String getPet(@PathVariable("petId") Long petId, Model model){
        PetInfoResDto petInfoResDto = petService.getPet(petId);

        model.addAttribute("petInfo", petInfoResDto);
        return "petInfo";
    }

}
