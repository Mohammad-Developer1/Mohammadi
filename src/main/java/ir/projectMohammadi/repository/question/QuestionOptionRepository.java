package ir.projectMohammadi.repository.question;

import ir.projectMohammadi.model.question.MultipleChoiceQuestion;
import ir.projectMohammadi.model.question.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {

    void deleteByQuestion(MultipleChoiceQuestion question);

}
