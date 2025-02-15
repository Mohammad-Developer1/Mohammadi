package ir.projectMohammadi.service.Student.impl;

import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.repository.student.IStudentRepository;
import ir.projectMohammadi.service.Student.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private IStudentRepository studentRepository;

    @Override
    public Boolean saveAndUpdateStudent(Student student) {
        if (student == null) {
            throw new NullPointerException("student is null");
        }else {
            studentRepository.save(student);
            return true;
        }
    }

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudent(Long id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }else {
            return studentRepository.findById(id).get();
        }
    }

    @Override
    public void deleteStudent(Long id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }else {
            studentRepository.deleteById(id);
        }
    }
}
