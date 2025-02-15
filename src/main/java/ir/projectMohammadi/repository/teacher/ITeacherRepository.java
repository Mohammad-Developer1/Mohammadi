package ir.projectMohammadi.repository.teacher;

import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.base.IBaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITeacherRepository extends IBaseRepository<Teacher,Long>  {
}
