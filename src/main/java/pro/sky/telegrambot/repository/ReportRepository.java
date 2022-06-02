package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Report;

import java.time.LocalDateTime;

public interface ReportRepository extends JpaRepository<Report, Long> {


    @Query(value = "select * from report where client_chat_id = ?1 and date_time_of_report = ?2", nativeQuery = true)
    Report findReportByClientAndDateTimeOfReport(long chatId, LocalDateTime localDateTime);

    @Transactional
    @Modifying
    @Query(value = "UPDATE report SET description = ?1, file_id = ?2, file_size = ?3 WHERE id = ?4", nativeQuery = true)
    void update(String caption, String fileId, Integer fileSize, Long id);
}
