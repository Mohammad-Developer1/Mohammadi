package ir.projectMohammadi.service.course.impl;

import ir.projectMohammadi.config.securityUtil.SecurityUtil;
import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.student.IStudentRepository;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.course.CourseViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements ICourseService {

    private final ICourseRepository courseRepository;

    private final ITeacherRepository teacherRepository;

    private final IStudentRepository studentRepository;

    @Override
    public Boolean saveCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("Course is null");
        }
        if (course.getTeacher() == null) {
            course.setTeacher(null);
        }
        String lastCourseCode = courseRepository.findLastCourseCode();

        String newCourseCode = generateNextCourseCode(lastCourseCode);
        course.setCourseCode(newCourseCode);

        courseRepository.save(course);
        return true;
    }

    private String generateNextCourseCode(String lastCourseCode) {
        if (lastCourseCode == null || lastCourseCode.isEmpty()) {
            return "CES001";
        }
        String numericPart = lastCourseCode.substring(3);
        int number = Integer.parseInt(numericPart);
        number++;
        return String.format("CES%03d", number);
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
    public List<CourseViewModel> getAllCourse() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {
            CourseViewModel courseViewModel = ModelMapper.map(course, CourseViewModel.class);
            courseViewModel.setTitle(course.getTitle());
            courseViewModel.setStartDate(course.getStartDate());
            courseViewModel.setEndDate(course.getEndDate());
            courseViewModel.setId(course.getID());

            if (course.getTeacher() != null) {
                courseViewModel.setTeacherID(course.getTeacher().getID());
                courseViewModel.setTeacherLastName(course.getTeacher().getLastName());
            }

            if (course.getStudents() != null) {
                List<String> studentNames = course.getStudents().stream()
                        .map(student -> student.getFirstName() + " " + student.getLastName())
                        .collect(Collectors.toList());

                courseViewModel.setStudentList(studentNames);
            }

            return courseViewModel;
        }).collect(Collectors.toList());
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

            course.getStudents().add(student);
            courseRepository.save(course);
            return true;
        }
        return false;
    }


    @Override
    public Optional<Course> findById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).get();
    }
    @Override
    public List<Course> getCoursesForLoggedInTeacher() {
        String username = SecurityUtil.getLoggedInUsername();

        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return courseRepository.findByTeacherWithDetails(teacher.getID());
    }

}
