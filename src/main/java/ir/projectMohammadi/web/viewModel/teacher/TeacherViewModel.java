package ir.projectMohammadi.web.viewModel.teacher;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.web.viewModel.base.PersonViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeacherViewModel extends PersonViewModel {


    private Set<Course> course;

    private Long administratorId;

    private String administratorLastName;
}
