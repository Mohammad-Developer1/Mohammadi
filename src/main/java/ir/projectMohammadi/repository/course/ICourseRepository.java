package ir.projectMohammadi.repository.course;

import ir.projectMohammadi.model.course.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {


    long countByTeacherID(@Param("teacherId") Long teacherId);


    boolean existsByCourseCode(String courseCode);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM course_student WHERE student_id = :studentId", nativeQuery = true)
    void removeStudentFromAllCourses(@Param("studentId") Long studentId);

    @Query("SELECT c.courseCode FROM Course c ORDER BY c.id DESC LIMIT 1")
    String findLastCourseCode();

    List<Course> findByTeacher_User_Username(String username);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.exams WHERE c.teacher.id = :teacherId")
    List<Course> findByTeacherWithDetails(@Param("teacherId") Long teacherId);


}
