package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Client;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getClientByChatId(Long chatId);

    List<Client> getClientsByAdoptionDateIsNull();

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET adoption_date = ?1 WHERE chat_id = ?2 and adoption_date is null ", nativeQuery = true)
    void setAdoptionDate(LocalDateTime currentTime, Long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET probation_date = ?1 WHERE chat_id = ?2 and adoption_date is not null ", nativeQuery = true)
    void setProbationDate(LocalDateTime probationDate, Long chatId);
}
