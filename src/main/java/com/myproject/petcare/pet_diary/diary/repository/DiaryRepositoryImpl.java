package com.myproject.petcare.pet_diary.diary.repository;

import com.myproject.petcare.pet_diary.diary.entity.Diary;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.myproject.petcare.pet_diary.diary.entity.QDiary.diary;
import static com.myproject.petcare.pet_diary.pet.entity.QPet.pet;

public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DiaryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Diary> findByPetIdAndDtype(List<Long> petIds, List<String> dtypes, Pageable pageable) {
        List<Diary> content = queryFactory
                .select(diary)
                .from(diary)
                .where(dtypeIn(dtypes))
                .where(petIdsIn(petIds))
                .orderBy(diary.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(diary.count())
                .from(diary)
                .where(dtypeIn(dtypes))
                .where(petIdsIn(petIds))
                .orderBy(diary.date.desc());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    private BooleanExpression petIdsIn(List<Long> petIds) {
        return petIds != null ? pet.id.in(petIds) : null;
    }

    private BooleanExpression dtypeIn(List<String> dtypes) {
        return dtypes != null ? diary.dtype.in(dtypes) : null;
    }
}
