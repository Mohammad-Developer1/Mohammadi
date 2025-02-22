package ir.projectMohammadi.web.controller.user;

import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.service.user.IUserService;
import ir.projectMohammadi.util.ChangeRoleRequest;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.util.ApiResponse;
import ir.projectMohammadi.web.viewModel.User.UserViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;


    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<ApiResponse> registerUser(@RequestBody User user) {
        try {
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
            return ResponseEntity.ok(new ApiResponse(true, "User registered successfully", userViewModel));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error registering user: " + e.getMessage()));
        }
    }


    @PutMapping("/changeRoleAndUpdate")
    @ResponseBody
    public ResponseEntity<ApiResponse> changeRoleAndUpdate(@RequestBody ChangeRoleRequest request) {
        try {
            User updatedUser = userService.changeUserRoleAndUpdateInfo(
                    request.getUserId(),
                    request.getNewRole(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getMobileNumber()
            );

            return ResponseEntity.ok(new ApiResponse(true, "User role updated successfully", updatedUser));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating role: " + e.getMessage()));
        }
    }


    @GetMapping("/pending")
    @ResponseBody
    public ResponseEntity<ApiResponse> getPendingUsers() {
        List<User> users = userService.getPendingUsers();
        List<UserViewModel> userViewModels = users.stream()
                .map(user -> ModelMapper.map(user, UserViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Pending users retrieved successfully", userViewModels));
    }

    @PutMapping("/approve/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponse> approveUser(@PathVariable Long userId) {
        try {
            userService.approveUser(userId);
            return ResponseEntity.ok(new ApiResponse(true, "User approved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Error approving user: " + e.getMessage()));
        }
    }

    @PutMapping("/reject/{userId}")
    @ResponseBody
    public ResponseEntity<ApiResponse> rejectUser(@PathVariable Long userId) {
        try {
            userService.rejectUser(userId);
            return ResponseEntity.ok(new ApiResponse(true, "User rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Error rejecting user: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<ApiResponse> searchUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName) {

        List<User> users = userService.searchUsers(role, username, firstName, lastName);
        List<UserViewModel> userViewModels = users.stream()
                .map(user -> ModelMapper.map(user, UserViewModel.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse(true, "Users retrieved successfully", userViewModels));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    @ResponseBody
    public ResponseEntity<ApiResponse> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "All users retrieved successfully", users));
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/profile")
    @ResponseBody
    public ResponseEntity<ApiResponse> getTeacherProfile(Authentication authentication) {
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "Profile retrieved successfully", user.get()));
    }

}
