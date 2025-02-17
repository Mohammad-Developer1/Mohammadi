package ir.projectMohammadi.service.user;

import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerUser(String username, String password, String firstName, String lastName, String email, String mobileNumber, Role role);

    List<User> getPendingUsers();

    User approveUser(Long userId);

    User rejectUser(Long userId);

    List<User> searchUsers(Role role, String username, String firstName, String lastName);

    List<User> findAll();

    Optional<User> findByUsername(String username);
}
