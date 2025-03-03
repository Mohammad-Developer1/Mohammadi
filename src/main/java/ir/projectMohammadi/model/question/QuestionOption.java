package ir.projectMohammadi.model.question;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.projectMohammadi.model.baseModel.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QUESTION_OPTION")
public class QuestionOption extends BaseEntity<Long> {

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean isCorrect;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private MultipleChoiceQuestion question;
}

