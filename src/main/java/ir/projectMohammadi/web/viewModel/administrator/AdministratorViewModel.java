package ir.projectMohammadi.web.viewModel.administrator;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.web.viewModel.base.PersonViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdministratorViewModel extends PersonViewModel {

    private Set<Course> courses;
}
