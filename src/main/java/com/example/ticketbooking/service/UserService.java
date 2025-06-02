package com.example.ticketbooking.service;

import com.example.ticketbooking.entity.UserEntity;
import com.example.ticketbooking.ResponseBean.UserResponseBean;
import com.example.ticketbooking.RequestBean.UserRequestBean;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserRequestBean addUser(@Valid @PathVariable UserRequestBean userRequestBean) {
        if (userRequestBean == null) {
            return null;
        }
        UserEntity userEntity = UserEntity.builder()
                .username(userRequestBean.getUsername())
                .email(userRequestBean.getEmail())
                .mobile(userRequestBean.getMobile())
                .gender(userRequestBean.getGender())
                .age(userRequestBean.getAge())
                .build();
        userRepository.save(userEntity);
        log.info("User Added Successfully");
    return userRequestBean;
    }

    public UserRequestBean updateUser(String email, UserRequestBean updatedUser) {
        UserEntity existingUserEntity = userRepository.findByEmail(email).orElse(null);
        if (existingUserEntity != null) {
            existingUserEntity.setUsername(updatedUser.getUsername());
            existingUserEntity.setEmail(updatedUser.getEmail());
            existingUserEntity.setMobile(updatedUser.getMobile());
            existingUserEntity.setGender(updatedUser.getGender());
            existingUserEntity.setAge(updatedUser.getAge());
            userRepository.save(existingUserEntity);
            log.info("User Updated Successfully");
        }else {
            log.info("User Not Found");
        }
        return updatedUser ;
    }

    public UserRequestBean getUserByEmail(String email) throws ResourceNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return UserRequestBean.builder()
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .gender(userEntity.getGender())
                .age(userEntity.getAge())
                .build();
    }

    public List<UserResponseBean> getAllUser() {
        List<UserResponseBean> userResponseBeans = new ArrayList<>();
        List<UserEntity> userEntityRepositoryAll = userRepository.findAll();
        for (UserEntity userEntity : userEntityRepositoryAll) {
            UserResponseBean userResponseBean = UserResponseBean.builder()
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .email(userEntity.getEmail())
                    .mobile(userEntity.getMobile())
                    .gender(userEntity.getGender())
                    .age(userEntity.getAge())
                    .build();
            userResponseBeans.add(userResponseBean);
            log.info("All Users get successfully {}", userResponseBeans.size());
        }
        return userResponseBeans;
    }

    public String deleteUserByEmail(String email) throws ResourceNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found with email: " + email));
        if (userEntity != null) {
            userRepository.delete(userEntity);
            log.info("User Deleted Successfully {}" , email);
            return "User Deleted Successfully " + userEntity.getEmail();
        }
        else {
            log.info("User Not Found {}", email);
        }
        return null;
    }
}
