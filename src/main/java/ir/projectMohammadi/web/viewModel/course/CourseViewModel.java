package ir.projectMohammadi.web.viewModel.course;

import ir.projectMohammadi.model.administor.Administrator;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.web.viewModel.base.BaseViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseViewModel extends BaseViewModel<Long> {

    private String title;

    private String startDate;

    private String endDate;

    private Teacher teacher;

    private Administrator administrator;
}
