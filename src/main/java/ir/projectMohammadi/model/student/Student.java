package ir.projectMohammadi.model.student;

import ir.projectMohammadi.model.baseModel.Person;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STUDENT")
public class Student extends Person {

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    public Student(String firstName, String lastName, String email, String mobileNumber) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setMobileNumber(mobileNumber);
    }


}
