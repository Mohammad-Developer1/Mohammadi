package ir.projectMohammadi.service.teacher;

import ir.projectMohammadi.model.teacher.Teacher;

import java.util.List;

public interface ITeacherService {

    Boolean saveAndUpdateTeacher(Teacher teacher);
    List<Teacher> getAllTeacher();
    Teacher getTeacher(Long id);
    Boolean deleteTeacherById(Long id);
}
