package ir.projectMohammadi.service.course.impl;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.service.course.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements ICourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public Boolean saveAndUpdateCourse(Course course) {
        if (course == null) {
            throw new NullPointerException("Course is null");
        }else {
            courseRepository.save(course);
            return true;
        }
    }

    @Override
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    @Override
    public Course getCourse(Long id) {
        if (id == null) {
            throw new NullPointerException("Course id is null");
        }else {
            return courseRepository.findById(id).get();
        }
    }

    @Override
    public void deleteCourse(Long id) {
        if (id == null) {
            throw new NullPointerException("Course id is null");
        }else {
            courseRepository.deleteById(id);
        }
    }
}
