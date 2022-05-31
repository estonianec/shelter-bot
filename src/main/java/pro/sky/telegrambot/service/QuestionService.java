package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import pro.sky.telegrambot.model.Question;

public interface QuestionService {
    void createNewQuestion(Message message);

    void createEmptyQuestion(Message message);

    boolean isQuestionExist(Long chatId);

    Question getOlderQuestion(Message message);

    boolean isItAnswer(Message message);

    Question makeAnswer(Message message);
}
