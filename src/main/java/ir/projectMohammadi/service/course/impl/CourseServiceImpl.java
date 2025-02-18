package ir.projectMohammadi.service.course.impl;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.student.IStudentRepository;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import ir.projectMohammadi.service.course.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private ITeacherRepository teacherRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    public Boolean saveCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("Course is null");
        }
        if (course.getTeacher() == null || course.getTeacher().getID() == null) {
            throw new IllegalArgumentException("Teacher ID is missing in course");
        }
        courseRepository.save(course);
        return true;
    }


    @Override
    public Boolean updateCourse(Long id, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isEmpty()) {
            throw new RuntimeException("Course not found with ID: " + id);
        }

        Course course = existingCourse.get();
        course.setTitle(updatedCourse.getTitle());
        course.setStartDate(updatedCourse.getStartDate());
        course.setEndDate(updatedCourse.getEndDate());

        if (updatedCourse.getTeacher() != null) {
            course.setTeacher(updatedCourse.getTeacher());
        }

        courseRepository.save(course);
        return true;
    }

    @Override
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourse(Long id) {
        if (id == null) {
            throw new NullPointerException("Course id is null");
        } else {
            return courseRepository.findById(id).get();
        }
    }

    @Override
    public Boolean deleteCourseById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
        return true;
    }
    @Override
    public boolean assignTeacher(Long courseId, Long teacherId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);

        if (courseOptional.isPresent() && teacherOptional.isPresent()) {
            Course course = courseOptional.get();
            Teacher teacher = teacherOptional.get();

            course.setTeacher(teacher);
            courseRepository.save(course);
            return true;
        }
        return false;
    }

    @Override
    public boolean addStudent(Long courseId, Long studentId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (courseOptional.isPresent() && studentOptional.isPresent()) {
            Course course = courseOptional.get();
            Student student = studentOptional.get();

            course.getStudents().add(student);  // اضافه کردن دانشجو به لیست دوره
            courseRepository.save(course);
            return true;
        }
        return false;
    }


}
