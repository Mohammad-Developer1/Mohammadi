package ir.projectMohammadi.service.question.impl;


import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.model.question.*;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.exam.ExamRepository;
import ir.projectMohammadi.repository.question.ExamQuestionRepository;
import ir.projectMohammadi.repository.question.QuestionOptionRepository;
import ir.projectMohammadi.repository.question.QuestionRepository;
import ir.projectMohammadi.repository.teacher.ITeacherRepository;
import ir.projectMohammadi.service.question.QuestionService;
import ir.projectMohammadi.web.viewModel.QuestionDTO;
import ir.projectMohammadi.web.viewModel.QuestionOptionDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ITeacherRepository teacherRepository;
    private final ExamRepository examRepository;
    private final ICourseRepository courseRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    @Override
    @Transactional
    public Question addQuestion(Question question, Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        question.setTeacher(teacher);
        question.setCourse(course);

        if (question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            if (mcq.getOptions() == null) {
                mcq.setOptions(new ArrayList<>());
            }
        }

        return questionRepository.save(question);
    }



    @Override
    public List<Question> getTeacherQuestionBank(Long teacherId, Long courseId) {
        return questionRepository.findByTeacher_IDAndCourse_ID(teacherId, courseId);
    }


    @Override
    @Transactional
    public void addQuestionToExam(Long questionId, Long examId, Integer score) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setExam(exam);
        examQuestion.setQuestion(question);
        examQuestion.setScore(score);

        examQuestionRepository.save(examQuestion);
    }

    @Override
    public QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO(
                question.getID(),
                question.getTitle(),
                question.getDescription(),
                question.getTeacher().getLastName(),
                question.getCourse().getTitle()
        );
    }

    @Override
    @Transactional
    public QuestionOption addOptionToQuestion(Long questionId, String optionText, boolean isCorrect) {
        MultipleChoiceQuestion question = (MultipleChoiceQuestion) questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        QuestionOption option = new QuestionOption();
        option.setText(optionText);
        option.setCorrect(isCorrect);
        option.setQuestion(question);

        questionOptionRepository.save(option);
        return option;
    }

    @Override
    public List<QuestionDTO> getAllQuestionsWithOptions() {
        List<Question> questions = questionRepository.findAll();

        return questions.stream().map(question -> {
            QuestionDTO dto = new QuestionDTO();
            dto.setId(question.getID());
            dto.setTitle(question.getTitle());
            dto.setDescription(question.getDescription());
            dto.setTeacherName(question.getTeacher().getLastName());
            dto.setCourseTitle(question.getCourse().getTitle());

            if (question instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = questionRepository.findByIdWithOptions(question.getID()).orElse((MultipleChoiceQuestion) question);

                List<QuestionOptionDTO> options = mcq.getOptions() != null ?
                        mcq.getOptions().stream()
                                .map(option -> new QuestionOptionDTO(option.getID(), option.getText(), option.isCorrect()))
                                .toList() :
                        new ArrayList<>();
                dto.setOptions(options);
            } else {
                dto.setOptions(new ArrayList<>());
            }

            return dto;
        }).toList();
    }


    @Override
    @Transactional
    public QuestionDTO addDescriptiveQuestion(String title, String description, int answerLengthLimit, Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        DescriptiveQuestion descriptiveQuestion = new DescriptiveQuestion();
        descriptiveQuestion.setTitle(title);
        descriptiveQuestion.setDescription(description);
        descriptiveQuestion.setAnswerLengthLimit(answerLengthLimit);
        descriptiveQuestion.setTeacher(teacher);
        descriptiveQuestion.setCourse(course);

        Question savedQuestion = questionRepository.save(descriptiveQuestion);

        return convertToDTO(savedQuestion);
    }

}

