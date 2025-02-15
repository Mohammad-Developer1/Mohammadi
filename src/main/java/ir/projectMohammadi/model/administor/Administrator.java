package ir.projectMohammadi.model.administor;

import ir.projectMohammadi.model.baseModel.Person;
import ir.projectMohammadi.model.course.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "ADMINISTRATOR")
public class Administrator extends Person {

    @OneToMany(mappedBy = "administrator")
    private Set<Course> courses;
}
