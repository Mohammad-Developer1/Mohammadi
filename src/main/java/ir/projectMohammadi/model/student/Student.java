package ir.projectMohammadi.model.student;

import ir.projectMohammadi.model.baseModel.Person;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDENT")
public class Student extends Person {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Student_Course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "Course_id")
    )
    private Set<Course> course;

}
