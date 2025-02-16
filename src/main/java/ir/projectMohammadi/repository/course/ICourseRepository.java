package ir.projectMohammadi.repository.course;

import ir.projectMohammadi.model.course.Course;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Course c WHERE c.ID = :id")
    int  deleteCourseById(@Param("id") Long id);

    default Boolean deleteCourse(Long id) {
        return deleteCourseById(id) > 0;
    }
}
