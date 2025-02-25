package ir.projectMohammadi.web.viewModel.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamViewModel {

    private String title;

    private String description;

    private Long duration;

    private String startDate;

    private String startTime;

    private Long courseId;
}
