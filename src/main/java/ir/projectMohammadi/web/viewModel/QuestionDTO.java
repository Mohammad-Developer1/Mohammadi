package ir.projectMohammadi.web.viewModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String teacherName;
    private String courseTitle;
    private List<QuestionOptionDTO> options;


    public QuestionDTO(Long id, String title, String description, String lastName, String title1) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacherName = lastName;
        this.courseTitle = title1;
    }
}

