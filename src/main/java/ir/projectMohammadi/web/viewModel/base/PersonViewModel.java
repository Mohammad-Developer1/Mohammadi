package ir.projectMohammadi.web.viewModel.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonViewModel extends BaseViewModel<Long> {


    private String firstName;


    private String lastName;


    private String email;


    private String mobileNumber;

    private Long userId;
}
