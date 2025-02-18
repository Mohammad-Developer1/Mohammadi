package ir.projectMohammadi.repository.course;

import ir.projectMohammadi.model.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT COUNT(c) FROM Course c WHERE c.teacher.ID = :teacherId")
    long countByTeacherId(@Param("teacherId") Long teacherId);


    boolean existsByCourseCode(String courseCode);
}
