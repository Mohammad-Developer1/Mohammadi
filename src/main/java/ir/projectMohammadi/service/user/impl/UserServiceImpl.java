package ir.projectMohammadi.service.user.impl;

import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.enums.status.Status;
import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.repository.User.UserRepository;
import ir.projectMohammadi.service.Student.IStudentService;
import ir.projectMohammadi.service.teacher.ITeacherService;
import ir.projectMohammadi.service.user.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    private final IStudentService studentService;

    private final ITeacherService teacherService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signUp(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

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
    @Transactional
    public User changeUserRoleAndUpdateInfo(Long userId, Role newRole, String firstName, String lastName, String email, String mobileNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == newRole) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setMobileNumber(mobileNumber);
            return userRepository.save(user);
        }

        if (user.getRole() == Role.TEACHER && newRole == Role.STUDENT) {
            teacherService.deleteTeacherByEmail(user.getEmail());
            Student student = new Student();
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setEmail(email);
            student.setMobileNumber(mobileNumber);
            student.setUser(user);
            studentService.saveAndUpdateStudent(student);
        }

        if (user.getRole() == Role.STUDENT && newRole == Role.TEACHER) {
            studentService.deleteStudentByEmail(user.getEmail());
            Teacher teacher = new Teacher();
            teacher.setFirstName(firstName);
            teacher.setLastName(lastName);
            teacher.setEmail(email);
            teacher.setMobileNumber(mobileNumber);
            teacher.setUser(user);
            teacherService.saveAndUpdateTeacher(teacher);
        }

        user.setRole(newRole);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setMobileNumber(mobileNumber);

        return userRepository.save(user);
    }






    @Override
    public List<User> getPendingUsers() {
        return userRepository.findByStatus(Status.PENDING);
    }

    @Override
    public User approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() == Status.ACCEPTED) {
            throw new RuntimeException("User is already approved.");
        }

        user.setStatus(Status.ACCEPTED);
        userRepository.save(user);

        if (user.getRole() == Role.TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setFirstName(user.getFirstName());
            teacher.setLastName(user.getLastName());
            teacher.setEmail(user.getEmail());
            teacher.setMobileNumber(user.getMobileNumber());
            teacher.setUser(user);
            teacherService.saveAndUpdateTeacher(teacher);
        }

        if (user.getRole() == Role.STUDENT) {
            Student student = new Student();
            student.setFirstName(user.getFirstName());
            student.setLastName(user.getLastName());
            student.setEmail(user.getEmail());
            student.setMobileNumber(user.getMobileNumber());
            student.setUser(user);
            studentService.saveAndUpdateStudent(student);
        }
        return user;
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

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }


}

