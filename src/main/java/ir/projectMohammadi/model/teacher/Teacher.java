package ir.projectMohammadi.model.teacher;

import ir.projectMohammadi.model.baseModel.Person;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "TEACHER")
public class Teacher extends Person {

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private Set<Course> courses;


    public Teacher(String firstName, String lastName, String email, String mobileNumber) {
        super.setFirstName(firstName);
        super.setLastName(lastName);
        super.setEmail(email);
        super.setMobileNumber(mobileNumber);
    }
}

