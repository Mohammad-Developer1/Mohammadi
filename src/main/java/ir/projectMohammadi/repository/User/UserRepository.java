package ir.projectMohammadi.repository.User;

import ir.projectMohammadi.model.user.User;
import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.enums.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByMobileNumber(String mobileNumber);

    List<User> findByStatus(Status status);

    List<User> findByRole(Role role);

    List<User> findByLastNameContainingIgnoreCase(String lastName);

    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByRoleAndLastNameContainingIgnoreCase(Role role, String lastName);

    List<User> findByRoleAndUsernameContainingIgnoreCase(Role role, String username);

    List<User> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    List<User> findByRoleAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(Role role, String firstName, String lastName);

    List<User> findByRoleAndUsernameContainingIgnoreCaseAndFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(Role role, String username, String firstName, String lastName);

    boolean existsByUsername(String username);
}

