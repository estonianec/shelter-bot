package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.ReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ClientRepository clientRepository;
    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(ReportRepository reportRepository, ClientRepository clientRepository) {
        this.reportRepository = reportRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void saveReport(Message message) {
        Report report = new Report();
        LocalDateTime nowDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        report.setDateTimeOfReport(nowDate);
        String caption = message.caption();
        report.setDescription(caption);
        String fileId = message.photo()[2].fileId();
        report.setFileId(fileId);
        Integer fileSize = message.photo()[2].fileSize();
        report.setFileSize(fileSize);
        logger.info("Saving report {}", report);
        Client client = clientRepository.getClientByChatId(message.chat().id());
        report.setClient(client);
        logger.info("Try to find client {}", client);
        Report oldReport = reportRepository.findReportByClientAndDateTimeOfReport(client.getChatId(), nowDate);
        if (oldReport != null) {
            reportRepository.update(caption, fileId, fileSize, oldReport.getId());
        } else {
            reportRepository.save(report);
        }
    }
}
