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

    @Transactional
    @Modifying
    @Query("DELETE FROM Student s WHERE s.ID = :id")
    int  deleteStudentById(@Param("id") Long id);

    default Boolean deleteStudentByID(Long id) {
        return deleteStudentById(id) > 0;
    }

    void deleteByEmail(String email);

    Optional<Student> findByEmail(String email);
}
