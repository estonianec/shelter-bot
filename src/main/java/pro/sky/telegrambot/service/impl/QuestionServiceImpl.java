package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Question;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.QuestionRepository;
import pro.sky.telegrambot.repository.VolunteerRepository;
import pro.sky.telegrambot.service.QuestionService;

import java.util.List;

import static pro.sky.telegrambot.constant.ButtonNameEnum.GET_QUESTION;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private TelegramBot telegramBot;
    private final QuestionRepository questionRepository;
    private final VolunteerRepository volunteerRepository;
    private final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    public QuestionServiceImpl(QuestionRepository questionRepository, VolunteerRepository volunteerRepository) {
        this.questionRepository = questionRepository;
        this.volunteerRepository = volunteerRepository;
    }

    Keyboard volunteerMenu = new ReplyKeyboardMarkup(
            GET_QUESTION.getButtonName())
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);

    @Override
    public void createNewQuestion(Message message) {
        questionRepository.createNewQuestion(message.chat().id(), message.text());
    }

    //Создаем пустое сообщение, как признак бота принять вопрос
    @Override
    public void createEmptyQuestion(Message message) {
        Question question = new Question();
        question.setChatId(message.chat().id());
        question.setAnswered(false);
        question.setAsked(false);
        questionRepository.save(question);
    }

    @Override
    public boolean isQuestionExist(Long chatId) {
        return questionRepository.getLastQuestion(chatId) != null;
    }

    @Override
    public Question getOlderQuestion(Message message) {
        Question question = questionRepository.getOlderQuestion();
        if (question != null) {
            questionRepository.markQuestionAsTaken(question.getId(), message.chat().id());
            return question;
        }
        return null;
    }

    @Override
    public boolean isItAnswer(Message message) {
        return questionRepository.getQuestionForAnswer(message.chat().id()) != null;
    }

    @Override
    public Question makeAnswer(Message message) {
        Question question = questionRepository.getQuestionForAnswer(message.chat().id());
        questionRepository.markQuestionAsAnswered(question.getId());
        return question;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotices() {
        List<Volunteer> listOfVolunteers = volunteerRepository.getListVolunteers();
        long chatId;
        if (questionRepository.getOlderQuestion() != null) {
            if (!listOfVolunteers.isEmpty()) {
                logger.info("Волонтеров доступно: " + listOfVolunteers.size());
                for (Volunteer listOfVolunteer : listOfVolunteers) {
                    chatId = listOfVolunteer.getChatId();
                    SendMessage request = new SendMessage(chatId, "К вам появились вопросики. Что бы ознакомиться и ответить, нажмите кнопку ниже.");
                    request.replyMarkup(volunteerMenu);
                    telegramBot.execute(request);
                }
            }
        }
    }
}
