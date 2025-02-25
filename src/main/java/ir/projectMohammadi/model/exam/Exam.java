package ir.projectMohammadi.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ir.projectMohammadi.model.baseModel.BaseEntity;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EXAM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "course"})
public class Exam extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Long duration;

    @Column
    private String startDate;

    @Column
    private String startTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id",nullable = false)
    @JsonIgnoreProperties({"exams", "students", "teacher"})
    private Course course;
}
