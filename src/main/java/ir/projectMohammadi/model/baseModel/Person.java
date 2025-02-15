package ir.projectMohammadi.model.baseModel;


import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person extends Base<Long> {


    @Column(   length = 50,nullable = false)
    private String firstName;


    @Column(  length = 50,nullable = false)
    private String lastName;

    @Column( length = 50,nullable = false)
    private String nationality;

    @Column( length = 10,nullable = false)
    private String nationalCode;

    @Column(length = 50,nullable = false)
    private String email;

    @Column( length = 11)
    private String phoneNumber;


}
