package com.myproject.petcare.pet_diary.user.entity;

import com.myproject.petcare.pet_diary.common.entity.BaseEntity;
import com.myproject.petcare.pet_diary.pet.entity.Pet;
import com.myproject.petcare.pet_diary.user.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Comment("사용자 ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    @Comment("이메일")
    private String email;

    @Column(nullable = false, length = 200)
    @Comment("비밀번호")
    private String password;

    @Column(nullable = false, length = 50)
    @Comment("이름")
    private String name;

    @Column(nullable = false, length = 20)
    @Comment("전화번호")
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    @Comment("역할(USER, ADMIN)")
    private Role role;

    @Column(columnDefinition = "TEXT")
    @Comment("리프레시 토근(JWT 저장)")
    private String refreshToken;
}
