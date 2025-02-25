package ir.projectMohammadi.web.viewModel.course;

import ir.projectMohammadi.web.viewModel.base.BaseViewModel;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseViewModel extends BaseViewModel<Long> {

        private Long id;

        private String courseCode;

        private String title;

        private String startDate;

        private String endDate;

        private Long teacherID;

        private String teacherLastName;

        private List<String> studentList;

        private List<ExamViewModel> examList;
    }



