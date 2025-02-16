package ir.projectMohammadi.web.viewModel.User;

import ir.projectMohammadi.model.enums.role.Role;
import ir.projectMohammadi.model.enums.status.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewModel {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String mobileNumber;

    private Role role;

    private Status status;
}

