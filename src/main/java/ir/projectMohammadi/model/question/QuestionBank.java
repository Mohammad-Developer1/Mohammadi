package ir.projectMohammadi.model.question;

import ir.projectMohammadi.model.baseModel.BaseEntity;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QUESTION_BANK")
public class QuestionBank extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course; // دوره‌ای که این سوال به آن تعلق دارد

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private Question question; // سوالی که در بانک سوالات این دوره ذخیره شده است
}
