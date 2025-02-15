package ir.projectMohammadi.repository.teacher.impl;

import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.base.impl.BaseRepositoryImpl;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import jakarta.persistence.EntityManager;

public class TeacherRepositoryImpl extends BaseRepositoryImpl<Teacher,Long> implements ITeacherRepository {

    @Override
    public Class<Teacher> getEntityClass() {
        return Teacher.class;
    }

    public TeacherRepositoryImpl(EntityManager entityManager) {
        super(Teacher.class, entityManager);
    }

}
