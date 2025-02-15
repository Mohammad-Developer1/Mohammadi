package ir.projectMohammadi.model.baseModel;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public abstract class Base<T> implements Serializable {

    @Id
    @GeneratedValue
    private T ID;


}
