package ir.projectMohammadi.service.question.impl;


import ir.projectMohammadi.model.course.Course;
import ir.projectMohammadi.model.exam.Exam;
import ir.projectMohammadi.model.question.*;
import ir.projectMohammadi.model.teacher.Teacher;
import ir.projectMohammadi.repository.course.ICourseRepository;
import ir.projectMohammadi.repository.exam.ExamRepository;
import ir.projectMohammadi.repository.question.ExamQuestionRepository;
import ir.projectMohammadi.repository.question.QuestionBankRepository;
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
    private final QuestionBankRepository questionBankRepository;

    @Override
    @Transactional
    public Question addQuestion(String title, String description, String questionType, Long teacherId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Question question;

        // ایجاد سوال بر اساس نوع آن (چندگزینه‌ای یا تشریحی)
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

        Question savedQuestion = questionRepository.save(question);

        // بررسی اینکه آیا سوال از قبل در بانک سوالات وجود دارد یا نه
        boolean exists = questionBankRepository.existsByCourseAndQuestion(course, savedQuestion);
        System.out.println("🔍 بررسی سوال در بانک سوالات: " + exists);

        // اگر سوال در بانک سوالات نبود، اضافه شود
        if (!exists) {
            QuestionBank questionBank = new QuestionBank();
            questionBank.setTitle(title); // مقداردهی تایتل بانک سوالات
            questionBank.setCourse(course);
            questionBank.setQuestion(savedQuestion);
            questionBankRepository.save(questionBank);
            System.out.println("✅ سوال در بانک سوالات اضافه شد.");
        } else {
            System.out.println("⚠ سوال از قبل در بانک سوالات موجود است.");
        }

        return savedQuestion;
    }

    @Override
    @Transactional
    public Question updateQuestion(Long questionId, String newTitle, String newDescription, Long teacherId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // بررسی اینکه استاد فقط سوالات خودش را ویرایش کند
        if (!question.getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only edit your own questions.");
        }

        // بروزرسانی اطلاعات سوال
        question.setTitle(newTitle);
        question.setDescription(newDescription);

        Question updatedQuestion = questionRepository.save(question);

        // اگر این سوال در بانک سوالات وجود دارد، اطلاعات آن را هم بروز کنیم
        Optional<QuestionBank> questionBank = questionBankRepository.findByQuestion(updatedQuestion);
        questionBank.ifPresent(qb -> {
            qb.setTitle(newTitle);
            questionBankRepository.save(qb);
        });

        return updatedQuestion;
    }


    @Override
    @Transactional
    public QuestionOption updateQuestionOption(Long optionId, String newText, boolean newIsCorrect, Long teacherId) {
        QuestionOption option = questionOptionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        // بررسی اینکه استاد فقط گزینه‌های سوالات خودش را ویرایش کند
        if (!option.getQuestion().getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only edit options for your own questions.");
        }

        // بروزرسانی اطلاعات گزینه
        option.setText(newText);
        option.setCorrect(newIsCorrect);

        return questionOptionRepository.save(option);
    }


    @Override
    @Transactional
    public void deleteQuestion(Long questionId, Long teacherId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        // بررسی اینکه استاد فقط سوالات خودش را حذف کند
        if (!question.getTeacher().getID().equals(teacherId)) {
            throw new RuntimeException("You can only delete your own questions.");
        }

        // بررسی اینکه آیا سوال در آزمون‌ها استفاده شده است
        boolean isUsedInExam = examQuestionRepository.existsByQuestion(question);
        if (isUsedInExam) {
            throw new RuntimeException("This question is used in an exam and cannot be deleted.");
        }

        // اگر سوال چندگزینه‌ای است، گزینه‌های آن را حذف کن
        if (question instanceof MultipleChoiceQuestion) {
            questionOptionRepository.deleteByQuestion((MultipleChoiceQuestion) question);
        }

        // حذف از بانک سوالات (اگر وجود داشته باشد)
        questionBankRepository.deleteByQuestion(question);

        // حذف خود سوال
        questionRepository.delete(question);
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

