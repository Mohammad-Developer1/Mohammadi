package ir.projectMohammadi.service.course;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.web.viewModel.course.CourseViewModel;

import java.util.List;
import java.util.Optional;

public interface ICourseService {
    Boolean saveCourse(Course course);
    Boolean updateCourse(Long id, Course updatedCourse);
    List<CourseViewModel> getAllCourse();
    Course getCourse(Long id);
    Boolean deleteCourseById(Long id);
    boolean assignTeacher(Long courseId, Long teacherId);
    boolean addStudent(Long courseId, Long studentId);
    List<Course> getCoursesForLoggedInTeacher();
    Optional<Course> findById(Long courseId);

    Course getCourseById(Long courseId);
}
