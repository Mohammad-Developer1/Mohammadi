package ir.projectMohammadi.service.user.impl;

import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.enums.status.Status;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.repository.UserRepository;
import ir.projectMohammadi.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(String username, String password, String firstName, String lastName, String email, String mobileNumber, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);
        user.setRole(role);
        user.setStatus(Status.PENDING);

        return userRepository.save(user);
    }

    @Override
    public List<User> getPendingUsers() {
        return userRepository.findByStatus(Status.PENDING);
    }

    @Override
    public User approveUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(Status.ACCEPTED);
        return userRepository.save(user);
    }

    @Override
    public User rejectUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(Status.REJECT);
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(Role role, String username, String firstName, String lastName) {
        if (role != null && username != null && firstName != null && lastName != null) {
            return userRepository.findByRoleAndUsernameContainingIgnoreCaseAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(role, username, firstName, lastName);
        } else if (role != null && firstName != null && lastName != null) {
            return userRepository.findByRoleAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(role, firstName, lastName);
        } else if (firstName != null && lastName != null) {
            return userRepository.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        } else if (role != null && username != null) {
            return userRepository.findByRoleAndUsernameContainingIgnoreCase(role, username);
        } else if (role != null && lastName != null) {
            return userRepository.findByRoleAndLastNameContainingIgnoreCase(role, lastName);
        } else if (username != null) {
            return userRepository.findByUsernameContainingIgnoreCase(username);
        } else if (role != null) {
            return userRepository.findByRole(role);
        } else if (firstName != null) {
            return userRepository.findByFirstNameContainingIgnoreCase(firstName);
        } else if (lastName != null) {
            return userRepository.findByLastNameContainingIgnoreCase(lastName);
        } else {
            return userRepository.findAll();
        }
    }
}

