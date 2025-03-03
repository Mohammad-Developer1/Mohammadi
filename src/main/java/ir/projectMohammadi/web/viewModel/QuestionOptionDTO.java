package ir.projectMohammadi.web.viewModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionDTO {
    private Long id;
    private String text;
    private boolean correct;



}


