package ir.projectMohammadi.service.exam;

import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.web.viewModel.exam.ExamDTO;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;

import java.util.List;

public interface ExamService {
    Exam createExam(Exam exam);
    void deleteExam(Long examId);
    List<Exam> getExamsByCourse(Long courseId);
    Exam updateExam(Long examId, ExamViewModel examViewModel);
    void addQuestionToExam(Long examId, Long questionId, Integer score);
    List<ExamDTO> getAllExamsWithQuestions();
    void setScore(Long examId, Long questionId, Integer score, Long teacherId);

    Integer getTotalScoreForExam(Long examId);
}
