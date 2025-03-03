package ir.projectMohammadi.web.viewModel.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionOptionDTO {
    private Long id;
    private String text; // فقط متن گزینه، بدون نمایش `isCorrect`
}

