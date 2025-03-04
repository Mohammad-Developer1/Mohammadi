package ir.projectMohammadi.repository.question;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.question.Question;
import ir.projectMohammadi.model.question.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank, Long> {
    List<QuestionBank> findByCourse(Course course); // دریافت سوالات یک دوره خاص
    boolean existsByCourseAndQuestion(Course course, Question question); // بررسی وجود یک سوال در بانک سوالات دوره
}
