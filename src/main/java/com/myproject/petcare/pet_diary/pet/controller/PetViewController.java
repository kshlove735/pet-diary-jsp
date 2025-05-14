package com.myproject.petcare.pet_diary.pet.controller;

import com.myproject.petcare.pet_diary.pet.dto.PartialPetReqDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PetViewController {

    @GetMapping("/pet")
    public String createPetPage(Model model) {
        model.addAttribute("partialPetReqDto", new PartialPetReqDto());
        return "createPet";
    }
}
