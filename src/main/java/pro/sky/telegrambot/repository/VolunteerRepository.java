package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Question;
import pro.sky.telegrambot.model.Volunteer;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    Volunteer getVolunteerByChatId(Long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE volunteer SET is_working = true WHERE chat_id = ?1", nativeQuery = true)
    void openJob(long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE volunteer SET is_working = false WHERE chat_id = ?1", nativeQuery = true)
    void closeJob(long chatId);

    @Query(value = "select * from volunteer where is_working = true", nativeQuery = true)
    List<Volunteer> getListVolunteers();
}
