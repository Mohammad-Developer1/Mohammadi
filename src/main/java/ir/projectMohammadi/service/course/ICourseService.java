package ir.projectMohammadi.service.course;

import ir.projectMohammadi.model.course.Course;

import java.util.List;

public interface ICourseService {
    Boolean saveCourse(Course course);
    Boolean updateCourse(Long id, Course updatedCourse);
    List<Course> getAllCourse();
    Course getCourse(Long id);
    Boolean deleteCourseById(Long id);
    boolean assignTeacher(Long courseId, Long teacherId);
    boolean addStudent(Long courseId, Long studentId);

}
