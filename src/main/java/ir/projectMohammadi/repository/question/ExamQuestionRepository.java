package ir.projectMohammadi.repository.question;

import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.model.question.ExamQuestion;
import ir.projectMohammadi.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    List<ExamQuestion> findByExam_ID(Long examId);

    List<ExamQuestion> findByExam(Exam exam);

    boolean existsByQuestion(Question question);

    Optional<ExamQuestion> findByExam_IDAndQuestion_ID(Long examId, Long questionId);

    @Query("SELECT SUM(eq.score) FROM ExamQuestion eq WHERE eq.exam.id = :examId")
    Integer getTotalScoreForExam(@Param("examId") Long examId);
}
