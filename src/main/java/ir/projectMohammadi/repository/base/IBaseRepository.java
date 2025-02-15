package ir.projectMohammadi.repository.base;

import org.springframework.data.jpa.repository.JpaRepository;
import java.io.Serializable;
import java.util.List;

public interface IBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    T saveAndUpdate(T t);
    Boolean deleteByID(ID id);
    List<T> findAll();
    T findByID(ID id);
}