package com.example.ticketbooking.service;

import com.example.ticketbooking.entity.UserEntity;
import com.example.ticketbooking.enum_package.Gender;
import com.example.ticketbooking.responseBean.UserResponseBean;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCriteriaService {

    private static final Logger log = LoggerFactory.getLogger(UserCriteriaService.class);
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
                    .age(userEntity.getAge())
                    .build();
            userResponseBeans.add(userResponseBean);
        }
        log.info("Users found: {}", userResponseBeans.size());
        return userResponseBeans;
    }
}
