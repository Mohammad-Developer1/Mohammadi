package ir.projectMohammadi.service.Student.impl;

import ir.projectMohammadi.model.student.Student;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.student.IStudentRepository;
import ir.projectMohammadi.service.Student.IStudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {

    private final IStudentRepository studentRepository;

    private final ICourseRepository courseRepository;

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
    public Boolean deleteStudent(Long id) {
        if (id == null) {
            throw new NullPointerException("id is null");
        }else {
          return studentRepository.deleteStudentByID(id);
        }
    }
    @Transactional
    public void deleteStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        courseRepository.removeStudentFromAllCourses(student.getID());

        studentRepository.delete(student);
    }



}
