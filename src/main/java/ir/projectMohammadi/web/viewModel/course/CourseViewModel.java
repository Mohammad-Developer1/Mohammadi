package ir.projectMohammadi.web.viewModel.course;

import ir.projectMohammadi.web.viewModel.base.BaseViewModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseViewModel extends BaseViewModel<Long> {

    private String courseCode; 

    private String title;

    private String startDate;

    private String endDate;

    private Long teacherID;

    private String teacherLastName;

    private List<String> studentList;

}
