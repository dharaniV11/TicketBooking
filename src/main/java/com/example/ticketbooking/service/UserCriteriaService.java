package com.example.ticketbooking.service;

import com.example.ticketbooking.entity.UserEntity;
import com.example.ticketbooking.enum_package.Gender;
import com.example.ticketbooking.responseBean.UserResponseBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserCriteriaService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<UserResponseBean> searchUsers(Gender gender, Integer minAge, Integer maxAge) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (gender != null) {
            predicates.add(criteriaBuilder.equal(root.get("gender"), gender));
        }

        if (minAge != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), minAge));
        }

        if (maxAge != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), maxAge));
        }

        query.select(root).where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

        TypedQuery<UserEntity> typedQuery = entityManager.createQuery(query);
        List<UserEntity> resultList = typedQuery.getResultList();
        List<UserResponseBean> userResponseBeans = new ArrayList<>();
        for (UserEntity userEntity : resultList) {
            UserResponseBean userResponseBean = UserResponseBean.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .email(userEntity.getEmail())
                    .mobile(userEntity.getMobile())
                    .gender(userEntity.getGender())
                    .build();
            userResponseBeans.add(userResponseBean);
        }
        log.info("Users found: {}", userResponseBeans.size());
        return userResponseBeans;
    }
}
