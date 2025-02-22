package ir.projectMohammadi.web.controller.student;

import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.service.Student.IStudentService;
import ir.projectMohammadi.util.mapper.ModelMapper;
import ir.projectMohammadi.web.viewModel.student.StudentViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {


    public final IStudentService studentService;

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

    @GetMapping("/getStudent/{id}")
    @ResponseBody
    public StudentViewModel getStudent(@PathVariable Long id) {
        return ModelMapper.map(studentService.getStudent(id),StudentViewModel.class);
    }

    @DeleteMapping("/deleteStudentById/{id}")
    @ResponseBody
    public Boolean deleteStudentById(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }

}
