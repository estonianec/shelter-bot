package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.Volunteer;

import java.util.List;

public interface VolunteerService {
    boolean isVolunteerExists(Long chatId);

    void openJob(Long chatId);

    void closeJob(Long chatId);

    List<Volunteer> getAllVolunteers();
}
