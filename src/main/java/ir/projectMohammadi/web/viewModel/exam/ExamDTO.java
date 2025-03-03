package ir.projectMohammadi.web.viewModel.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamDTO {
    private Long id;
    private String title;
    private String description;
    private String courseTitle;
    private String teacherName;
    private Long duration;
    private String startDate;
    private String startTime;
    private List<ExamQuestionDTO> questions;
}

