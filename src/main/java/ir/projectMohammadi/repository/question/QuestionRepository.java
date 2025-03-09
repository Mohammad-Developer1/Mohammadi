package ir.projectMohammadi.repository.question;


import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.question.MultipleChoiceQuestion;
import ir.projectMohammadi.model.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    List<Question> findByTeacher_IDAndCourse_ID(Long teacherId, Long courseId);

    @Query("SELECT q FROM MultipleChoiceQuestion q LEFT JOIN FETCH q.options WHERE q.id = :questionId")
    Optional<MultipleChoiceQuestion> findByIdWithOptions(@Param("questionId") Long questionId);


    boolean existsByTitleAndCourse(String title, Course course);



}
