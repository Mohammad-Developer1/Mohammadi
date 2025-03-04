package ir.projectMohammadi.repository.question;

import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.model.question.ExamQuestion;
import ir.projectMohammadi.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExam_ID(Long examId);

    List<ExamQuestion> findByExam(Exam exam);

    boolean existsByQuestion(Question question);
}
