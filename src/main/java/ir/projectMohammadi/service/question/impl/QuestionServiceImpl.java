package ir.projectMohammadi.service.question.impl;


import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.question.DescriptiveQuestion;
import ir.projectMohammadi.model.question.MultipleChoiceQuestion;
import ir.projectMohammadi.model.question.Question;
import ir.projectMohammadi.model.question.QuestionOption;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Question addQuestion(String title, String description, String questionType ,Long courseId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        boolean exists = questionRepository.existsByTitleAndCourse(title, course);
        if (exists) {
            throw new RuntimeException("This question already exists in the question bank for this course.");
        }

        Question question;
        if ("MULTIPLE_CHOICE".equalsIgnoreCase(questionType)) {
            question = new MultipleChoiceQuestion();
        } else if ("DESCRIPTIVE".equalsIgnoreCase(questionType)) {
            question = new DescriptiveQuestion();
        } else {
            throw new RuntimeException("Invalid question type.");
        }

        question.setTitle(title);
        question.setDescription(description);
        question.setTeacher(teacher);
        question.setCourse(course);

        return questionRepository.save(question);
    }



    @Override
    @Transactional
    public void deleteQuestionOption(Long optionId, Long teacherId) {
        QuestionOption option = questionOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        if (!option.getQuestion().getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only delete options for your own questions.");
        }

        questionOptionRepository.delete(option);
    }


    @Override
    @Transactional
    public Question updateQuestion(Long questionId, String newTitle, String newDescription) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getTeacher().getID().equals(teacher.getID())) {
            throw new RuntimeException("You can only edit your own questions.");
        }

        question.setTitle(newTitle);
        question.setDescription(newDescription);

        return questionRepository.save(question);
    }



    @Override
    @Transactional
    public QuestionOption updateQuestionOption(Long optionId, String newText, boolean newIsCorrect, Long teacherId) {
        QuestionOption option = questionOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        if (!option.getQuestion().getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only edit options for your own questions.");
        }

        option.setText(newText);
        option.setCorrect(newIsCorrect);

        return questionOptionRepository.save(option);
    }


    @Override
    @Transactional
    public void deleteQuestion(Long questionId, Long teacherId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (!question.getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only delete your own questions.");
        }

        boolean isUsedInExam = examQuestionRepository.existsByQuestion(question);
        if (isUsedInExam) {
            throw new RuntimeException("This question is used in an exam and cannot be deleted.");
        }

        if (question instanceof MultipleChoiceQuestion) {
            questionOptionRepository.deleteByQuestion((MultipleChoiceQuestion) question);
        }

        questionRepository.delete(question);
    }

    @Override
    public List<Question> getTeacherQuestionBank(Long teacherId, Long courseId) {
        return questionRepository.findByTeacher_IDAndCourse_ID(teacherId, courseId);
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

