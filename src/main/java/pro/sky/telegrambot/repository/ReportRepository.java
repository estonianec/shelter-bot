package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query(value = "select * from report where client_chat_id = ?1 order by date_time_of_report desc LIMIT 1", nativeQuery = true)
    Report getLastReportOfClient(Long chatId);
}
