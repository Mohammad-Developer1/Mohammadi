package ir.projectMohammadi.web.controller;

import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.service.Student.IStudentService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.student.StudentViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    public IStudentService studentService;

    @PostMapping("/saveStudent")
    @ResponseBody
    public Boolean saveStudent(@RequestBody StudentViewModel studentViewModel) {
        return studentService.saveAndUpdateStudent(ModelMapper.map(studentViewModel,Student.class));
    }

    @GetMapping("/getAllStudents")
    @ResponseBody
    public List<StudentViewModel> getAllStudents() {
        return ModelMapper.mapList(studentService.getAllStudent(),StudentViewModel.class);
    }


}
