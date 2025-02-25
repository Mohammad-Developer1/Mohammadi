package ir.projectMohammadi.web.controller.course;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.util.ApiResponse;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.course.CourseViewModel;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/Course")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @PostMapping("/saveCourse")
    @ResponseBody
    public ResponseEntity<ApiResponse> saveCourse(@RequestBody CourseViewModel courseViewModel) {
        try {

            if (courseViewModel.getTeacherID() != null) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "A teacher cannot be assigned at course creation."));
            }
            Course course = ModelMapper.map(courseViewModel, Course.class);
            course.setTeacher(null);
            boolean success = courseService.saveCourse(course);
            if (success) {
                return ResponseEntity.ok(new ApiResponse(true, "Course saved successfully", course));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse(false, "Failed to save course"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error saving course: " + e.getMessage()));
        }
    }





    @PutMapping("/updateCourse/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse> updateCourse(@PathVariable Long id, @RequestBody CourseViewModel courseViewModel) {
        try {
            Course course = ModelMapper.map(courseViewModel, Course.class);
            boolean success = courseService.updateCourse(id, course);

            if (success) {
                return ResponseEntity.ok(new ApiResponse(true, "Course updated successfully", course));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Course not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating course: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteCourseById/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse> deleteCourse(@PathVariable Long id) {
        try {
            boolean success = courseService.deleteCourseById(id);
            if (success) {
                return ResponseEntity.ok(new ApiResponse(true, "Course deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Course not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deleting course: " + e.getMessage()));
        }
    }


    @GetMapping("/getAllCourses")
    @ResponseBody
    public ResponseEntity<ApiResponse> getAllCourses() {
        List<CourseViewModel> courses = ModelMapper.mapList(courseService.getAllCourse(), CourseViewModel.class);
        return ResponseEntity.ok(new ApiResponse(true, "Courses retrieved successfully", courses));
    }


    @GetMapping("/getCourse/{id}")
    @ResponseBody
    public ResponseEntity<ApiResponse> getCourse(@PathVariable Long id) {
        try {
            CourseViewModel courseViewModel = ModelMapper.map(courseService.getCourse(id), CourseViewModel.class);
            return ResponseEntity.ok(new ApiResponse(true, "Course retrieved successfully", courseViewModel));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Course not found"));
        }
    }


    @PutMapping("/assign-teacher/{courseId}/{teacherId}")
    @ResponseBody
    public ResponseEntity<ApiResponse> assignTeacherToCourse(@PathVariable Long courseId, @PathVariable Long teacherId) {
        try {
            boolean result = courseService.assignTeacher(courseId, teacherId);
            if (result) {
                return ResponseEntity.ok(new ApiResponse(true, "Teacher assigned successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Failed to assign teacher. Check IDs."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error assigning teacher: " + e.getMessage()));
        }
    }

    @PutMapping("/add-student/{courseId}/{studentId}")
    @ResponseBody
    public ResponseEntity<ApiResponse> addStudentToCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            boolean result = courseService.addStudent(courseId, studentId);
            if (result) {
                return ResponseEntity.ok(new ApiResponse(true, "Student added to course successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Failed to add student. Check IDs."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error adding student: " + e.getMessage()));
        }
    }

    @GetMapping("/getMyCourses")
    @ResponseBody
    public ResponseEntity<List<CourseViewModel>> getMyCourses() {
        List<Course> courses = courseService.getCoursesForLoggedInTeacher();
        List<CourseViewModel> courseViewModels = courses.stream().map(course -> {
            CourseViewModel courseViewModel = ModelMapper.map(course, CourseViewModel.class);

            // مقداردهی اطلاعات استاد
            if (course.getTeacher() != null) {
                courseViewModel.setTeacherID(course.getTeacher().getID());
                courseViewModel.setTeacherLastName(course.getTeacher().getLastName());
            }

            // مقداردهی لیست دانشجوها
            if (course.getStudents() != null) {
                List<String> studentNames = course.getStudents().stream()
                        .map(student -> student.getFirstName() + " " + student.getLastName())
                        .collect(Collectors.toList());
                courseViewModel.setStudentList(studentNames);
            }

            // مقداردهی فقط عنوان و توضیحات آزمون‌ها
            if (course.getExams() != null) {
                List<ExamViewModel> examList = course.getExams().stream()
                        .map(exam -> {
                            ExamViewModel examViewModel = new ExamViewModel();
                            examViewModel.setTitle(exam.getTitle());
                            examViewModel.setDescription(exam.getDescription());
                            return examViewModel;
                        })
                        .collect(Collectors.toList());
                courseViewModel.setExamList(examList);
            } else {
                courseViewModel.setExamList(new ArrayList<>());
            }

            return courseViewModel;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(courseViewModels);
    }



}
