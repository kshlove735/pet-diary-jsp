package com.myproject.petcare.pet_diary.pet.controller;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.PartialPetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @PostMapping("/pet")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<PetInfoResDto> createPet(
            @RequestBody @Validated PartialPetReqDto partialPetReqDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        PetInfoResDto petInfoResDto = petService.createPet(partialPetReqDto, customUserDetails);

        return new ResponseDto<>(true, "반려견 등록 성공", petInfoResDto);
    }

    @GetMapping("/pet/{petId}")
    public ResponseDto<PetInfoResDto> getPet(@PathVariable("petId") Long petId) {

        PetInfoResDto petInfoResDto = petService.getPet(petId);

        return new ResponseDto<>(true, "반려견 단일 조회 성공", petInfoResDto);
    }

    @GetMapping("/pet")
    public ResponseDto<List<PetInfoResDto>> getPets(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        List<PetInfoResDto> petInfoResDtos = petService.getPets(customUserDetails);

        return new ResponseDto<>(true, "반려견 복수 조회 성공", petInfoResDtos);
    }

    @PutMapping("/pet/{petId}")
    public ResponseDto<PetInfoResDto> updatePet(
            @PathVariable("petId") Long petId,
            @RequestBody @Validated PartialPetReqDto partialPetReqDto) {

        PetInfoResDto petInfoResDto = petService.updatePet(petId, partialPetReqDto);

        return new ResponseDto<>(true, "반려견 정보 수정 조회 성공", petInfoResDto);
    }

    @DeleteMapping("/pet/{petId}")
    public ResponseDto<PetInfoResDto> deletePet(
            @PathVariable("petId") Long petId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        petService.deletePet(petId, customUserDetails);

        return new ResponseDto<>(true, "반려견 정보 삭제 성공", null);
    }
}
