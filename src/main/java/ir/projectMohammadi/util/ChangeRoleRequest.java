package ir.projectMohammadi.util;

import ir.projectMohammadi.model.enums.role.Role;
import lombok.Data;

@Data
public class ChangeRoleRequest {
    private Long userId;
    private Role newRole;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
}
