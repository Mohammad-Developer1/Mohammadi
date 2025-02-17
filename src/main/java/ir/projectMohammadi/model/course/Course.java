package ir.projectMohammadi.model.course;


import ir.projectMohammadi.model.baseModel.Base;
import ir.projectMohammadi.model.teacher.Teacher;
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
@Table(name = "COURSE")
public class Course extends Base<Long> {

    @Column
    private String title;

    @Column
    private String startDate;

    @Column
    private String endDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade=CascadeType.REMOVE)
    @JoinColumn(name = "teacher")
    private Teacher teacher;


}
