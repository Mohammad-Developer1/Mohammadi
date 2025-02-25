package ir.projectMohammadi.service.exam.impl;

import ir.projectMohammadi.config.securityUtil.SecurityUtil;
import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.exam.ExamRepository;
import ir.projectMohammadi.service.exam.ExamService;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService  {
    private final ExamRepository examRepository;
    private final ICourseRepository courseRepository;

    @Override
    public Exam createExam(Exam exam) {
        if (exam == null || exam.getCourse() == null) {
            throw new IllegalArgumentException("Exam and Course cannot be null");
        }
        String username = SecurityUtil.getLoggedInUsername();

        Course course = courseRepository.findById(exam.getCourse().getID())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (course.getTeacher() == null || !course.getTeacher().getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to create an exam for this course.");
        }
        exam.setCourse(course);
        return examRepository.save(exam);
    }


    @Override
    public List<Exam> getExamsByCourse(Long courseId) {
        String username = SecurityUtil.getLoggedInUsername();

        return examRepository.findByCourse_Teacher_User_Username(username)
                .stream()
                .filter(exam -> exam.getCourse().getID().equals(courseId))
                .collect(Collectors.toList());
    }


    @Override
    public List<ExamViewModel> getExamsByCourseId(Long courseId) {
        List<Exam> exams = examRepository.findByCourse_ID(courseId);
        return exams.stream().map(exam -> new ExamViewModel(
                exam.getTitle(),
                exam.getDescription(),
                exam.getDuration(),
                exam.getStartDate(),
                exam.getStartTime(),
                exam.getID()
        )).collect(Collectors.toList());
    }

    @Override
    public void deleteExam(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new RuntimeException("Exam not found");
        }
        examRepository.deleteById(examId);
    }
}


