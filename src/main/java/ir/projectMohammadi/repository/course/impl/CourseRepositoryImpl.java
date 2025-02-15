package ir.projectMohammadi.repository.course.impl;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.repository.base.impl.BaseRepositoryImpl;
import ir.projectMohammadi.repository.course.ICourseRepository;
import jakarta.persistence.EntityManager;

public class CourseRepositoryImpl extends BaseRepositoryImpl<Course,Long> implements ICourseRepository {
    @Override
    public Class<Course> getEntityClass() {
        return null;
    }

    public CourseRepositoryImpl(EntityManager entityManager) {
        super(Course.class, entityManager);
    }
}
