package ir.projectMohammadi.web.controller;

import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.service.course.ICourseService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.course.CourseViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/Course")
public class CourseController {

    @Autowired
    private ICourseService courseService;


    @PostMapping("/saveCourse")
    @ResponseBody
    public Boolean saveCourse(@RequestBody CourseViewModel courseViewModel) {
        return courseService.saveAndUpdateCourse(ModelMapper.map(courseViewModel, Course.class));
    }

    @GetMapping("/getAllCourses")
    @ResponseBody
    public List<CourseViewModel> getAllCourses() {
        return ModelMapper.mapList(courseService.getAllCourse(), CourseViewModel.class);
    }

    @GetMapping("/getCourse/{id}")
    @ResponseBody
    public CourseViewModel getCourse(@PathVariable Long id) {
        return ModelMapper.map(courseService.getCourse(id), CourseViewModel.class);
    }

    @DeleteMapping("/deleteCourseById/{id}")
    @ResponseBody
    public Boolean deleteCourseById(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }

}
