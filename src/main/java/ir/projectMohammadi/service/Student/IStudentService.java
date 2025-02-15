package ir.projectMohammadi.service.Student;

import ir.projectMohammadi.model.student.Student;

import java.util.List;

public interface IStudentService {
    Boolean saveAndUpdateStudent(Student student);
    List<Student> getAllStudent();
    Student getStudent(Long id);
    void deleteStudent(Long id);
}
