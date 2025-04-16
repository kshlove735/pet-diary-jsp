package com.myproject.petcare.pet_diary.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Comment("생성일시")
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @Comment("수정일시")
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
}
