package ir.projectMohammadi.web.viewModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDTO {
    private String title;
    private String description;
    private String questionType;
    private Long teacherId;
    private Long courseId;
}

