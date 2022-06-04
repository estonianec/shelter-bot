package pro.sky.telegrambot.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;
import pro.sky.telegrambot.service.VolunteerService;

import java.util.List;

@Service
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public VolunteerServiceImpl(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Override
    public boolean isVolunteerExists(Long chatId) {
        return volunteerRepository.getVolunteerByChatId(chatId) != null;
    }

    @Override
    public void openJob(Long chatId) {
        volunteerRepository.openJob(chatId);
    }

    @Override
    public void closeJob(Long chatId) {
        volunteerRepository.closeJob(chatId);
    }

    @Override
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.getAllBy();
    }

}
