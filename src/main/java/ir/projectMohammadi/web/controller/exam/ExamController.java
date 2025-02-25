package ir.projectMohammadi.web.controller.exam;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.service.exam.ExamService;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;
    private final ICourseService courseService;

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createExam(@RequestBody ExamViewModel examViewModel) {
        try {
            Exam exam = new Exam();
            exam.setTitle(examViewModel.getTitle());
            exam.setDescription(examViewModel.getDescription());
            exam.setDuration(examViewModel.getDuration());
            exam.setStartDate(examViewModel.getStartDate());
            exam.setStartTime(examViewModel.getStartTime());

            Course course = courseService.findById(examViewModel.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            exam.setCourse(course);

            return ResponseEntity.ok(examService.createExam(exam));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: " + e.getMessage());
        }
    }


    // دریافت تمام آزمون‌های یک دوره که توسط معلم لاگین‌شده ایجاد شده‌اند
    @GetMapping("/by-course/{courseId}")
    @ResponseBody
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }



    @GetMapping("/getExamsByCourseId/{courseId}")
    @ResponseBody
    public ResponseEntity<List<ExamViewModel>> getExamsByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourseId(courseId));
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }
}
