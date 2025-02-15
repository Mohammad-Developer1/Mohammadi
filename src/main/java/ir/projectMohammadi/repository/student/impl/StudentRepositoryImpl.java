package ir.projectMohammadi.repository.student.impl;

import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.repository.base.impl.BaseRepositoryImpl;
import ir.projectMohammadi.repository.student.IStudentRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;


@Repository
public class StudentRepositoryImpl extends BaseRepositoryImpl<Student, Long> implements IStudentRepository {
    @Override
    public Class<Student> getEntityClass() {
        return Student.class;
    }

    public StudentRepositoryImpl(EntityManager entityManager) {
        super(Student.class, entityManager);
    }
}
