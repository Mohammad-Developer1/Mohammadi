package ir.projectMohammadi.repository.exam;

import ir.projectMohammadi.model.exam.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByCourse_Title(String courseTitle);

    List<Exam> findByCourse_ID(Long courseId);

    List<Exam> findByCourse_Teacher_User_Username(String username);
}
