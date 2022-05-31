package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Volunteer;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Volunteer getVolunteerByChatId(Long chatId);
}
