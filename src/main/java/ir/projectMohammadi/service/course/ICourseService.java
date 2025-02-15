package ir.projectMohammadi.service.course;

import ir.projectMohammadi.model.course.Course;

import java.util.List;

public interface ICourseService {
    Boolean saveAndUpdateCourse(Course course);
    List<Course> getAllCourse();
    Course getCourse(Long id);
    void deleteCourse(Long id);
}
