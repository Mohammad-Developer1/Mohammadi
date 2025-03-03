package ir.projectMohammadi.model.question;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("DESCRIPTIVE")
public class DescriptiveQuestion extends Question {

    @Column(nullable = false)
    private int answerLengthLimit;
}
