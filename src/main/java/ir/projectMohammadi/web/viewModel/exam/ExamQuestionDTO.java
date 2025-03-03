package ir.projectMohammadi.web.viewModel.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String questionType;
    private Integer score;
    private List<ExamQuestionOptionDTO> options; // لیست گزینه‌ها (فقط متن، بدون نمایش پاسخ صحیح)
}

