package ir.projectMohammadi.service.exam.impl;

import ir.projectMohammadi.config.securityUtil.SecurityUtil;
import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.model.question.ExamQuestion;
import ir.projectMohammadi.model.question.MultipleChoiceQuestion;
import ir.projectMohammadi.model.question.Question;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.exam.ExamRepository;
import ir.projectMohammadi.repository.question.ExamQuestionRepository;
import ir.projectMohammadi.repository.question.QuestionRepository;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import ir.projectMohammadi.service.exam.ExamService;
import ir.projectMohammadi.web.viewModel.exam.ExamDTO;
import ir.projectMohammadi.web.viewModel.exam.ExamQuestionDTO;
import ir.projectMohammadi.web.viewModel.exam.ExamQuestionOptionDTO;
import ir.projectMohammadi.web.viewModel.exam.ExamViewModel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService  {
    private final ExamRepository examRepository;
    private final ICourseRepository courseRepository;

    private final QuestionRepository questionRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ITeacherRepository teacherRepository;

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
    @Transactional
    public void setScore(Long examId, Long questionId, Integer score, Long teacherId) {
        ExamQuestion examQuestion = examQuestionRepository.findByExam_IDAndQuestion_ID(examId, questionId)
                .orElseThrow(() -> new RuntimeException("ExamQuestion not found"));

        if (!examQuestion.getExam().getCourse().getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only set scores for your own exams.");
        }

        examQuestion.setScore(score);
        examQuestionRepository.save(examQuestion);
    }

    @Override
    public Integer getTotalScoreForExam(Long examId) {
        return examQuestionRepository.getTotalScoreForExam(examId);
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
    public void deleteExam(Long examId) {
        if (!examRepository.existsById(examId)) {
            throw new RuntimeException("Exam not found");
        }
        examRepository.deleteById(examId);
    }

    @Override
    public Exam updateExam(Long examId, ExamViewModel examViewModel) {
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        String username = SecurityUtil.getLoggedInUsername();
        if (!existingExam.getCourse().getTeacher().getUser().getUsername().equals(username)) {
            throw new RuntimeException("You are not authorized to edit this exam.");
        }

        existingExam.setTitle(examViewModel.getTitle());
        existingExam.setDescription(examViewModel.getDescription());
        existingExam.setDuration(examViewModel.getDuration());
        existingExam.setStartDate(examViewModel.getStartDate());
        existingExam.setStartTime(examViewModel.getStartTime());

        return examRepository.save(existingExam);
    }

    @Override
    @Transactional
    public void addQuestionToExam(Long examId, Long questionId, Integer score) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // بررسی اینکه آیا استاد صاحب سوال است
        if (!question.getTeacher().getID().equals(teacher.getID())) {
            throw new RuntimeException("You can only add your own questions to the exam.");
        }

        // بررسی اینکه آیا استاد صاحب آزمون است
        if (!exam.getCourse().getTeacher().getID().equals(teacher.getID())) {
            throw new RuntimeException("You can only modify your own exams.");
        }

        // بررسی اینکه آیا سوال و آزمون به یک دوره تعلق دارند
        if (!question.getCourse().getID().equals(exam.getCourse().getID())) {
            throw new RuntimeException("The question must belong to the same course as the exam.");
        }

        // بررسی اینکه آیا سوال قبلاً به این آزمون اضافه شده است
        boolean exists = examQuestionRepository.existsByExamAndQuestion(exam, question);
        if (exists) {
            throw new RuntimeException("This question is already added to the exam.");
        }

        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(exam);
        examQuestion.setQuestion(question);
        examQuestion.setScore(score);
        examQuestionRepository.save(examQuestion);
    }




    @Override
    public List<ExamDTO> getAllExamsWithQuestions() {
        List<Exam> exams = examRepository.findAll();

        return exams.stream().map(exam -> {
            ExamDTO examDTO = new ExamDTO();
            examDTO.setId(exam.getID());
            examDTO.setTitle(exam.getTitle());
            examDTO.setDescription(exam.getDescription());
            examDTO.setDuration(exam.getDuration());
            examDTO.setStartDate(exam.getStartDate());
            examDTO.setStartTime(exam.getStartTime());
            examDTO.setCourseTitle(exam.getCourse().getTitle());

            if (exam.getCourse().getTeacher() != null) {
                examDTO.setTeacherName(exam.getCourse().getTeacher().getLastName());
            } else {
                examDTO.setTeacherName("بدون استاد");
            }

            List<ExamQuestionDTO> examQuestions = examQuestionRepository.findByExam(exam).stream()
                    .map(examQuestion -> {
                        Question question = examQuestion.getQuestion();
                        List<ExamQuestionOptionDTO> options = new ArrayList<>();

                        String questionType = question.getClass().getSimpleName();
                        if (questionType.equals("MultipleChoiceQuestion")) {
                            questionType = "MULTIPLE_CHOICE";
                            options = ((MultipleChoiceQuestion) question).getOptions().stream()
                                    .map(option -> new ExamQuestionOptionDTO(option.getID(), option.getText()))
                                    .toList();
                        } else if (questionType.equals("DescriptiveQuestion")) {
                            questionType = "DESCRIPTIVE";
                        } else {
                            questionType = "UNKNOWN";
                        }

                        return new ExamQuestionDTO(
                                question.getID(),
                                question.getTitle(),
                                question.getDescription(),
                                questionType,
                                examQuestion.getScore(),
                                options
                        );
                    })
                    .toList();

            examDTO.setQuestions(examQuestions);
            return examDTO;
        }).toList();
    }
}