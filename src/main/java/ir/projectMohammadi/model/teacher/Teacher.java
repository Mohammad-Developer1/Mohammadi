package ir.projectMohammadi.model.teacher;

import ir.projectMohammadi.model.administor.Administrator;
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
@Table(name = "TEACHER")
public class Teacher extends Person {

    @OneToMany(mappedBy = "teacher",fetch = FetchType.LAZY)
    private Set<Course> course;

    @OneToOne(fetch = FetchType.LAZY)
    private Administrator administrator;
}
