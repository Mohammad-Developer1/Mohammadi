package ir.projectMohammadi.web.controller.question;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.question.QuestionBankRepository;
import ir.projectMohammadi.web.viewModel.QuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questionBank")
@RequiredArgsConstructor
public class QuestionBankController {

    private final QuestionBankRepository questionBankRepository;
    private final ICourseRepository courseRepository;

    @GetMapping("/getQuestionBankByCourse")
    public ResponseEntity<List<QuestionDTO>> getQuestionBankByCourse(@RequestParam Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<QuestionDTO> questions = questionBankRepository.findByCourse(course).stream()
                .map(qb -> new QuestionDTO(
                        qb.getQuestion().getID(),
                        qb.getQuestion().getTitle(),
                        qb.getQuestion().getDescription(),
                        qb.getQuestion().getTeacher().getLastName(),
                        qb.getCourse().getTitle()
                ))
                .toList();

        return ResponseEntity.ok(questions);
    }
}
