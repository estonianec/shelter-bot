package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Question;
import pro.sky.telegrambot.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value = "select * from report where client_chat_id = ?1 order by date_time_of_report desc LIMIT 1", nativeQuery = true)
    Report getLastReportOfClient(Long chatId);

    @Query(value = "select * from report where is_reviewed = false and volunteer_chat_id is null order by id LIMIT 1", nativeQuery = true)
    Report getOlderReport();

    @Transactional
    @Modifying
    @Query(value = "UPDATE report SET volunteer_chat_id = ?2, is_reviewed = true WHERE id = ?1", nativeQuery = true)
    void markReportAsReviewed(long id, long volunteer_chat_id);
}
