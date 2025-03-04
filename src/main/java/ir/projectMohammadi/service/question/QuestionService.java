package ir.projectMohammadi.service.question;

import ir.projectMohammadi.model.question.Question;
import ir.projectMohammadi.model.question.QuestionOption;
import ir.projectMohammadi.web.viewModel.QuestionDTO;

import java.util.List;

public interface QuestionService {

    Question addQuestion(String title, String description, String questionType, Long teacherId, Long courseId);

    List<Question> getTeacherQuestionBank(Long teacherId, Long courseId);

    void addQuestionToExam(Long questionId, Long examId, Integer score);

    QuestionDTO convertToDTO(Question question);

    QuestionOption addOptionToQuestion(Long questionId, String optionText, boolean isCorrect);

    List<QuestionDTO> getAllQuestionsWithOptions();

    QuestionDTO addDescriptiveQuestion(String title, String description, int answerLengthLimit, Long teacherId, Long courseId);

}

