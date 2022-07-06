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
    @Query(value = "UPDATE client SET adoption_date = ?1, adoption_status = 1 WHERE chat_id = ?2 and adoption_date is null ", nativeQuery = true)
    void setAdoptionDate(LocalDateTime currentTime, Long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET probation_date = ?1 WHERE chat_id = ?2", nativeQuery = true)
    void setProbationDate(LocalDateTime probationDate, Long chatId);

    @Query(value = "select * from client where adoption_status = 1", nativeQuery = true)
    List<Client> getListOfClientsWithAnimal(LocalDateTime currentTime);

    @Query(value = "select * from client where adoption_status = 1 and probation_date <= ?1", nativeQuery = true)
    List<Client> getListOfClientsWithAnimalAndProbationIsEnd(LocalDateTime currentTime);

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET adoption_status = ?1 WHERE chat_id = ?2", nativeQuery = true)
    void setAdoptionStatus(int newStatus, Long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET probation_date = ?1 WHERE chat_id = ?2", nativeQuery = true)
    void addProbationDays(LocalDateTime newDate, Long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE client SET animal_type = ?2 WHERE chat_id = ?1", nativeQuery = true)
    void setAnimalType(Long chatId, int type);
}
