package ir.projectMohammadi.repository.teacher;

import ir.projectMohammadi.model.teacher.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeacherRepository extends JpaRepository<Teacher, Long> {


    @Transactional
    @Modifying
    @Query("DELETE FROM Teacher t WHERE t.ID = :id")
    int  deleteTeacherById(@Param("id") Long id);

    default Boolean deleteTeacher(Long id) {
        return deleteTeacherById(id) > 0;
    }


    void deleteByEmail(String email);
}
