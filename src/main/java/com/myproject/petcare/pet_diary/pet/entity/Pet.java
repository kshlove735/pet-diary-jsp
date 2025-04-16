package com.myproject.petcare.pet_diary.pet.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.diary.entity.Diary;
import com.myproject.petcare.pet_diary.pet.enums.Gender;
import com.myproject.petcare.pet_diary.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    @Comment("반려견 ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 ID")
    private User user;

    @Column(nullable = false, length = 50)
    @Comment("이름")
    private String name;

    @Column(nullable = false, length = 50)
    @Comment("품종")
    private String breed;

    @Column(nullable = false)
    @Comment("생년월일")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("성별(MALE, FEMALE)")
    private Gender gender;

    @Column(precision = 5, scale = 2)
    @Comment("체중")
    private BigDecimal weight;

    @Column(columnDefinition = "TEXT")
    @Comment("특이사항")
    private String description;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Comment("반려동물의 일기 목록")
    private List<Diary> diaries = new ArrayList<>();

    public void changeUser(User user) {
        this.user = user;
        user.getPets().add(this);
    }
}
