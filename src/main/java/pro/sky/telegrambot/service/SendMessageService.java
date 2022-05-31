package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface SendMessageService {

    SendMessage answerMessage(Message message);
}
