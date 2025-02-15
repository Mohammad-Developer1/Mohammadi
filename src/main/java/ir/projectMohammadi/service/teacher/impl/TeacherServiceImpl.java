package ir.projectMohammadi.service.teacher.impl;

import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import ir.projectMohammadi.service.teacher.ITeacherService;
import ir.projectMohammadi.web.viewModel.teacher.TeacherViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherServiceImpl implements ITeacherService {

    @Autowired
    private ITeacherRepository teacherRepository;

    @Override
    public Teacher saveAndUpdateTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new NullPointerException("Teacher is null");
        }else {
           return teacherRepository.save(teacher);
        }
    }

    @Override
    public List<Teacher> getAllTeacher() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacher(Long id) {
        if (id == null) {
            throw new NullPointerException("Teacher id is null");
        }else {
            return teacherRepository.findById(id).get();
        }
    }

    @Override
    public void deleteTeacher(Long id) {
        if (id == null) {
            throw new NullPointerException("Teacher id is null");
        }else {
            teacherRepository.deleteById(id);
        }
    }
}
