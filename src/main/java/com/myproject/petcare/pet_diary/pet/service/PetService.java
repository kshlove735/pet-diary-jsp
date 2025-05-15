package com.myproject.petcare.pet_diary.pet.service;

import com.myproject.petcare.pet_diary.common.exception.custom_exception.NotFoundException;
import com.myproject.petcare.pet_diary.common.exception.custom_exception.UnauthorizedException;
import com.myproject.petcare.pet_diary.jwt.CustomUserDetails;
import com.myproject.petcare.pet_diary.pet.dto.PartialPetReqDto;
import com.myproject.petcare.pet_diary.pet.dto.PetInfoResDto;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.pet.repository.PetRepository;
import com.myproject.petcare.pet_diary.user.entity.User;
import com.myproject.petcare.pet_diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public PetInfoResDto createPet(PartialPetReqDto partialPetReqDto, CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);

        Pet pet = new Pet();
        pet.setName(partialPetReqDto.getName());
        pet.setBreed(partialPetReqDto.getBreed());
        pet.setBirthDate(partialPetReqDto.getBirthDate());
        pet.setGender(partialPetReqDto.getGender());
        pet.setWeight(partialPetReqDto.getWeight());
        pet.setDescription(partialPetReqDto.getDescription());
        pet.changeUser(user);
        petRepository.save(pet);

        return getPetInfoResDto(pet);
    }

    public PetInfoResDto getPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));
        return getPetInfoResDto(pet);
    }

    public List<PetInfoResDto> getPets(CustomUserDetails customUserDetails) {
        User user = getUserFromUserDetails(customUserDetails);
        List<Pet> pets = user.getPets();

        List<PetInfoResDto> list = new ArrayList<>();
        for (Pet pet : pets) {
            list.add(getPetInfoResDto(pet));
        }
        return list;
    }

    @Transactional
    public PetInfoResDto updatePet(Long petId, PartialPetReqDto partialPetReqDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        pet.setName(partialPetReqDto.getName());
        pet.setBreed(partialPetReqDto.getBreed());
        pet.setBirthDate(partialPetReqDto.getBirthDate());
        pet.setGender(partialPetReqDto.getGender());
        pet.setWeight(partialPetReqDto.getWeight());

        if(StringUtils.hasText(partialPetReqDto.getDescription())){
            pet.setDescription(partialPetReqDto.getDescription());
        }else{
            pet.setDescription(null);
        }

        return getPetInfoResDto(pet);
    }

    @Transactional
    public void deletePet(Long petId, CustomUserDetails customUserDetails) {
        // 반려견 조회
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new NotFoundException("해당하는 반려견이 없습니다."));

        // 권한 확인 : 현재 사용자가 해당 Pet의 소유자인지 확인
        if(!Objects.equals(pet.getUser().getId(), Long.valueOf(customUserDetails.getUsername()))){
            throw new UnauthorizedException("해당 반려견을 삭제할 권한이 없습니다.");
        }
        petRepository.delete(pet);
    }

    private User getUserFromUserDetails(CustomUserDetails customUserDetails) {
        Long id = Long.valueOf(customUserDetails.getUsername());
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    private PetInfoResDto getPetInfoResDto(Pet pet) {
        return new PetInfoResDto(
                pet.getId(), pet.getName(), pet.getBreed(), pet.getBirthDate(),
                pet.getGender(), pet.getWeight(), pet.getDescription(), pet.getCreateDate(), pet.getUpdatedDate());
    }
}
