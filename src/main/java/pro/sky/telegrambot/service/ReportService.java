package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import pro.sky.telegrambot.model.Report;

public interface ReportService {


    Report getOlderReport(Message message);

    void saveReport(Message message);
}
