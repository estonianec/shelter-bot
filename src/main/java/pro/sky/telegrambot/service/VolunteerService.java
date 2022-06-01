package pro.sky.telegrambot.service;

public interface VolunteerService {
    boolean isVolunteerExists(Long chatId);

    void openJob(Long chatId);

    void closeJob(Long chatId);
}
