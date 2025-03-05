package ir.projectMohammadi.web.controller.question;

import ir.projectMohammadi.model.question.Question;
import ir.projectMohammadi.model.question.QuestionOption;
import ir.projectMohammadi.service.question.QuestionService;
import ir.projectMohammadi.util.ApiResponse;
import ir.projectMohammadi.web.viewModel.QuestionCreateDTO;
import ir.projectMohammadi.web.viewModel.QuestionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @PostMapping("/addQuestion")
    @ResponseBody
    public ResponseEntity<QuestionDTO> addQuestion(
            @RequestBody QuestionCreateDTO questionDTO) {

        Question savedQuestion = questionService.addQuestion(
                questionDTO.getTitle(),
                questionDTO.getDescription(),
                questionDTO.getQuestionType(),
                questionDTO.getTeacherId(),
                questionDTO.getCourseId()
        );

        return ResponseEntity.ok(questionService.convertToDTO(savedQuestion));
    }

    @PutMapping("/editQuestion")
    @ResponseBody
    public ResponseEntity<QuestionDTO> editQuestion(
            @RequestParam Long questionId,
            @RequestParam String newTitle,
            @RequestParam String newDescription,
            @RequestParam Long teacherId) {

        Question updatedQuestion = questionService.updateQuestion(questionId, newTitle, newDescription, teacherId);
        return ResponseEntity.ok(questionService.convertToDTO(updatedQuestion));
    }

    @PutMapping("/editOption")
    @ResponseBody
    public ResponseEntity<ApiResponse> editQuestionOption(
            @RequestParam Long optionId,
            @RequestParam String newText,
            @RequestParam boolean newIsCorrect,
            @RequestParam Long teacherId) {

        try {
            QuestionOption updatedOption = questionService.updateQuestionOption(optionId, newText, newIsCorrect, teacherId);
            return ResponseEntity.ok(new ApiResponse(true, "Option updated successfully", updatedOption));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error updating option: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteQuestion")
    @ResponseBody
    public ResponseEntity<ApiResponse> deleteQuestion(
            @RequestParam Long questionId,
            @RequestParam Long teacherId) {

        try {
            questionService.deleteQuestion(questionId, teacherId);
            return ResponseEntity.ok(new ApiResponse(true, "Question and its options deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error deleting question: " + e.getMessage()));
        }
    }

    @DeleteMapping("/deleteOption")
    @ResponseBody
    public ResponseEntity<ApiResponse> deleteQuestionOption(
            @RequestParam Long optionId,
            @RequestParam Long teacherId) {

        try {
            questionService.deleteQuestionOption(optionId, teacherId);
            return ResponseEntity.ok(new ApiResponse(true, "Option deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Error deleting option: " + e.getMessage()));
        }
    }

    @GetMapping("/bank/{courseId}")
    @ResponseBody
    public ResponseEntity<List<QuestionDTO>> getTeacherQuestionBank(
            @RequestParam Long teacherId,
            @PathVariable Long courseId) {

        List<Question> questions = questionService.getTeacherQuestionBank(teacherId, courseId);
        List<QuestionDTO> questionDTOs = questions.stream()
                .map(questionService::convertToDTO)
                .toList();

        return ResponseEntity.ok(questionDTOs);
    }

    @PostMapping("/addOption")
    @ResponseBody
    public ResponseEntity<ApiResponse> addOptionToQuestion(
            @RequestParam("questionId") Long questionId,
            @RequestParam("optionText") String optionText,
            @RequestParam("isCorrect") boolean isCorrect) {
        try {
            QuestionOption option = questionService.addOptionToQuestion(questionId, optionText, isCorrect);
            return ResponseEntity.ok(new ApiResponse(true, "Option added successfully", option));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error adding option: " + e.getMessage()));
        }
    }

    @GetMapping("/getAllQuestions")
    @ResponseBody
    public ResponseEntity<List<QuestionDTO>> getAllQuestionsWithOptions() {
        List<QuestionDTO> questions = questionService.getAllQuestionsWithOptions();
        return ResponseEntity.ok(questions);
    }

    @PostMapping("/addDescriptiveQuestion")
    @ResponseBody
    public ResponseEntity<QuestionDTO> addDescriptiveQuestion(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int answerLengthLimit,
            @RequestParam Long teacherId,
            @RequestParam Long courseId) {
        QuestionDTO savedQuestion = questionService.addDescriptiveQuestion(title, description, answerLengthLimit, teacherId, courseId);
        return ResponseEntity.ok(savedQuestion);
    }




}
