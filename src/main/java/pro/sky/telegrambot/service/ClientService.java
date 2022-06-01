package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;

public interface ClientService {
    boolean isClientExists(Long chatId);

    void createNewClient(Message message);
}
