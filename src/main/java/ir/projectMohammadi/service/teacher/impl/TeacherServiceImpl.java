package ir.projectMohammadi.service.teacher.impl;

import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
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

    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public Boolean saveAndUpdateTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new NullPointerException("Teacher is null");
        }else {
            teacherRepository.save(teacher);
            return true;
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
    public Boolean deleteTeacherById(Long id) {
        if (id == null) {
            throw new NullPointerException("Teacher ID is null");
        }

        long courseCount = courseRepository.countByTeacherId(id);
        if (courseCount > 0) {
            throw new RuntimeException("Cannot delete teacher with assigned courses.");
        }

        return teacherRepository.deleteTeacher(id);
    }

}
