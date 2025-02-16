package ir.projectMohammadi.web.controller;


import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.service.user.IUserService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.User.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    public IUserService userService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserViewModel> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRole()
        );

        UserViewModel userViewModel = ModelMapper.map(newUser, UserViewModel.class);
        return ResponseEntity.ok(userViewModel);
    }
    @GetMapping("/pending")
    @ResponseBody
    public ResponseEntity<List<UserViewModel>> getPendingUsers() {
        List<User> users = userService.getPendingUsers();
        List<UserViewModel> userViewModels = users.stream()
                .map(user -> ModelMapper.map(user, UserViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userViewModels);
    }


    @PutMapping("/approve/{userId}")
    @ResponseBody
    public ResponseEntity<String> approveUser(@PathVariable Long userId) {
        userService.approveUser(userId);
        return ResponseEntity.ok("User approved successfully.");
    }


    @PutMapping("/reject/{userId}")
    @ResponseBody
    public ResponseEntity<String> rejectUser(@PathVariable Long userId) {
        userService.rejectUser(userId);
        return ResponseEntity.ok("User rejected successfully.");
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserViewModel>> searchUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        System.out.println("🔍 Request received at /search: role=" + role + ", username=" + username + ", firstName=" + firstName + ", lastName=" + lastName);

        List<User> users = userService.searchUsers(role, username, firstName, lastName);

        List<UserViewModel> userViewModels = users.stream()
                .map(user -> ModelMapper.map(user, UserViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userViewModels);
    }

}

