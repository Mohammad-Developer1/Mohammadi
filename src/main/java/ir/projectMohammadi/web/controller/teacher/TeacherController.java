package ir.projectMohammadi.web.controller.teacher;

import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.service.teacher.ITeacherService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.teacher.TeacherViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private ITeacherService teacherService;


    @PostMapping("/saveTeacher")
    @ResponseBody
    public Boolean saveTeacher(@RequestBody TeacherViewModel teacherViewModel) {
        return teacherService.saveAndUpdateTeacher(ModelMapper.map(teacherViewModel, Teacher.class));
    }

    @GetMapping("/getAllTeachers")
    @ResponseBody
    public List<TeacherViewModel> getAllTeachers() {
        return ModelMapper.mapList(teacherService.getAllTeacher(),TeacherViewModel.class);
    }


    @GetMapping("/getTeacher/{id}")
    @ResponseBody
    public TeacherViewModel getTeacher(@PathVariable Long id) {
        return ModelMapper.map(teacherService.getTeacher(id),TeacherViewModel.class);
    }

    @DeleteMapping("/deleteTeacherById/{id}")
    @ResponseBody
    public Boolean deleteTeacherById (@PathVariable Long id ){
      return  teacherService.deleteTeacherById(id);
    }

}
