package com.example.ticketbooking.ResponseBean;

import com.example.ticketbooking.enum_package.Gender;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;


@Data
@Builder
public class UserResponseBean {

    private UUID id;
    private String username;
    private String email;
    private String mobile;
    private Gender gender;
    private int age;
}
