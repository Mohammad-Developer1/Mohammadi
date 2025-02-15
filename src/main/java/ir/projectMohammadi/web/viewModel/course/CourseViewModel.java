package ir.projectMohammadi.web.viewModel.course;

import ir.projectMohammadi.web.viewModel.base.BaseViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseViewModel extends BaseViewModel<Long> {

    private String title;

    private String startDate;

    private String endDate;

    private Long teacherID;

    private String teacherLastName;

    private Long administratorId;

    private String administratorLastName;
}
