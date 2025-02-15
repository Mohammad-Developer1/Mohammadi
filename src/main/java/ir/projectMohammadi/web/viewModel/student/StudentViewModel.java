package ir.projectMohammadi.web.viewModel.student;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.web.viewModel.base.PersonViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentViewModel extends PersonViewModel {

    private Set<Course> course;
}
