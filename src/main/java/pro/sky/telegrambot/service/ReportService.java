package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;

public interface ReportService {

    void saveReport(Message message);
}
