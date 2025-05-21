package com.example.ticketbooking.controller;

import com.example.ticketbooking.RequestBean.UserRequestBean;
import com.example.ticketbooking.ResponseBean.UserResponseBean;
import com.example.ticketbooking.enum_package.Gender;
import com.example.ticketbooking.exception.ResourceNotFoundException;
import com.example.ticketbooking.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserCriteriaService userFilterService;

    @PostMapping("/addUser")
    public ResponseEntity<EntityModel<UserRequestBean>> addUser(@Valid @RequestBody UserRequestBean userRequestBean) throws ResourceNotFoundException {
        UserRequestBean savedUser = userService.addUser(userRequestBean);

        EntityModel<UserRequestBean> resource = EntityModel.of(savedUser,
                linkTo(methodOn(UserController.class).getUserByEmail(userRequestBean.getEmail())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUser()).withRel("all-users"));
//                linkTo(methodOn(UserController.class).updateUser(savedUser.getEmail(), null)).withRel("update"),
//                linkTo(methodOn(UserController.class).deleteUser(savedUser.getEmail())).withRel("delete"));

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserByEmail(savedUser.getEmail())).toUri())
                .body(resource);
    }
    @PutMapping("/updateUser/{email}")
    public UserRequestBean updateUser(@PathVariable String email, @RequestBody UserRequestBean updatedUser) {
        return userService.updateUser(email, updatedUser);
    }

    @GetMapping("/search")
    public List<UserResponseBean> searchUser(@RequestParam(required = false)Gender gender,
                                       @RequestParam(required = false) Integer MinAge,
                                       @RequestParam(required = false) Integer MaxAge)
    {
        return userFilterService.searchUsers(gender,MinAge,MaxAge);
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<UserRequestBean> getUserByEmail(@PathVariable String email) throws ResourceNotFoundException {
        UserRequestBean user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAllUser")
    public List<UserResponseBean> getAllUser() {
        return userService.getAllUser();
    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) throws ResourceNotFoundException {
        String result = userService.deleteUserByEmail(email);
        return ResponseEntity.ok(result);
    }

}
