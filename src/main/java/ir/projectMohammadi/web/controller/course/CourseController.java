package ir.projectMohammadi.web.controller.course;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.course.CourseViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/Course")
public class CourseController {

    @Autowired
    private ICourseService courseService;


    @PostMapping("/saveCourse")
    @ResponseBody
    public Boolean saveCourse(@RequestBody CourseViewModel courseViewModel) {
        Course course = ModelMapper.map(courseViewModel, Course.class);

        if (courseViewModel.getTeacherID() != null) {
            Teacher teacher = new Teacher();
            teacher.setID(courseViewModel.getTeacherID());
            course.setTeacher(teacher);
        }

        return courseService.saveCourse(course);
    }


    @PutMapping("/updateCourse/{id}")
    @ResponseBody
    public Boolean updateCourse(@PathVariable Long id, @RequestBody CourseViewModel courseViewModel) {
        Course course = ModelMapper.map(courseViewModel, Course.class);
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/deleteCourseById/{id}")
    @ResponseBody
    public Boolean deleteCourse(@PathVariable Long id) {
        return courseService.deleteCourseById(id);
    }



    @GetMapping("/getAllCourses")
    @ResponseBody
    public List<CourseViewModel> getAllCourses() {
        return ModelMapper.mapList(courseService.getAllCourse(), CourseViewModel.class);
    }


    @GetMapping("/getCourse/{id}")
    @ResponseBody
    public CourseViewModel getCourse(@PathVariable Long id) {
        return ModelMapper.map(courseService.getCourse(id), CourseViewModel.class);
    }

    @PutMapping("/assign-teacher/{courseId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<String> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        boolean result = courseService.assignTeacher(courseId, teacherId);
        if (result) {
            return ResponseEntity.ok("Teacher assigned successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to assign teacher. Check IDs.");
        }
    }

    @PutMapping("/add-student/{courseId}/{studentId}")
    @ResponseBody
    public ResponseEntity<String> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        boolean result = courseService.addStudent(courseId, studentId);
        if (result) {
            return ResponseEntity.ok("Student added to course successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add student. Check IDs.");
        }
    }



}
