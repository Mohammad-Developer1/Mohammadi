package ir.projectMohammadi.repository.student;

import ir.projectMohammadi.model.student.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Long> {


    Boolean  deleteStudentByID(@Param("id") Long id);

    void deleteByEmail(String email);

    Optional<Student> findByEmail(String email);

}
