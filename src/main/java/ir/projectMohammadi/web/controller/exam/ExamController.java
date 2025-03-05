package ir.projectMohammadi.web.controller.exam;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.service.exam.ExamService;
import ir.projectMohammadi.util.ApiResponse;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.exam.ExamDTO;
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
    @PutMapping("/update/{examId}")
    @ResponseBody
    public ResponseEntity<ApiResponse> updateExam(@PathVariable Long examId, @RequestBody ExamViewModel examViewModel) {
        try {
            Exam updatedExam = examService.updateExam(examId, examViewModel);

            ExamViewModel updatedExamViewModel = ModelMapper.map(updatedExam, ExamViewModel.class);
            updatedExamViewModel.setCourseId(updatedExam.getCourse().getID());

            return ResponseEntity.ok(new ApiResponse(true, "Exam updated successfully.", updatedExamViewModel));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating exam: " + e.getMessage(), null));
        }
    }

    @GetMapping("/getExamsByCourseId/{courseId}")
    @ResponseBody
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(examService.getExamsByCourse(courseId));
    }


    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addQuestionToExam")
    @ResponseBody
    public ResponseEntity<ApiResponse> addQuestionToExam(
            @RequestParam Long examId,
            @RequestParam Long questionId,
            @RequestParam Long teacherId,
            @RequestParam Integer score) {
        try {
            examService.addQuestionToExam(examId, questionId, teacherId, score);
            return ResponseEntity.ok(new ApiResponse(true, "Question added to exam successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error adding question to exam: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllExams")
    @ResponseBody
    public ResponseEntity<List<ExamDTO>> getAllExamsWithQuestions() {
        List<ExamDTO> exams = examService.getAllExamsWithQuestions();
        return ResponseEntity.ok(exams);
    }

    @PutMapping("/setScore")
    @ResponseBody
    public ResponseEntity<ApiResponse> setScore(
            @RequestParam Long examId,
            @RequestParam Long questionId,
            @RequestParam Integer score,
            @RequestParam Long teacherId) {

        try {
            examService.setScore(examId, questionId, score, teacherId);
            return ResponseEntity.ok(new ApiResponse(true, "Score set successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error setting score: " + e.getMessage()));
        }
    }

    @GetMapping("/getTotalScore")
    @ResponseBody
    public ResponseEntity<Integer> getTotalScore(@RequestParam Long examId) {
        Integer totalScore = examService.getTotalScoreForExam(examId);
        return ResponseEntity.ok(totalScore);
    }
}
