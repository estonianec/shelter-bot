package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.ReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pro.sky.telegrambot.constant.ButtonNameEnum.GET_QUESTION;
import static pro.sky.telegrambot.constant.ButtonNameEnum.GET_REPORT;


@Service
public class ReportServiceImpl implements ReportService  {
    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    LocalDateTime penaltyTime;
    Report lastReport;

    private final ReportRepository reportRepository;
    private final ClientServiceImpl clientService;
    private final VolunteerServiceImpl volunteerService;
    private final ClientRepository clientRepository;
    @Autowired
    private TelegramBot telegramBot;

    public ReportServiceImpl(ReportRepository reportRepository, ClientServiceImpl clientService, VolunteerServiceImpl volunteerService, ClientRepository clientRepository) {
        this.reportRepository = reportRepository;
        this.clientService = clientService;
        this.volunteerService = volunteerService;
        this.clientRepository = clientRepository;
    }

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    Keyboard volunteerMenu = new ReplyKeyboardMarkup(
            GET_REPORT.getButtonName())
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);

    @Scheduled(cron = "0 0 0/1 * * *")
    public void sendNotices() {
        sendNoticesToClientsAboutReports();
        sendNoticesToVolunteersAboutReports();
        sendNoticesToVolunteersAboutEndOfProbationDates();
    }

    public void sendNoticesToClientsAboutReports() {
        List<Client> listOfClientsWithAnimal = clientService.getListOfClientsWithAnimal();
        penaltyTime = currentTime.minusDays(1);
        if (!listOfClientsWithAnimal.isEmpty()) {
            for (Client client : listOfClientsWithAnimal) {
                lastReport = reportRepository.getLastReportOfClient(client.getChatId());
                if ((lastReport == null && !client.getAdoptionDate().isEqual(currentTime)) || (lastReport != null && lastReport.getDateTimeOfReport().isBefore(penaltyTime))) {
                    SendMessage sendMessage = new SendMessage(client.getChatId(), "Вы вчера не отправили отчет о питомце. Пожалуйста, отправьте отчёт, иначе мы будем вынуждены принять меры");
                    telegramBot.execute(sendMessage);
                }
            }
        }
    }

    public void sendNoticesToVolunteersAboutReports() {
        List<Client> listOfClientsWithAnimal = clientService.getListOfClientsWithAnimal();
        penaltyTime = currentTime.minusDays(2);
        List<Volunteer> listOfVolunteers = volunteerService.getAllVolunteers();
        if (!listOfClientsWithAnimal.isEmpty()) {
            for (Client client : listOfClientsWithAnimal) {
                lastReport = reportRepository.getLastReportOfClient(client.getChatId());
                if ((lastReport == null && !client.getAdoptionDate().isAfter(penaltyTime)) || (lastReport != null && lastReport.getDateTimeOfReport().isBefore(penaltyTime))) {
                    for (Volunteer listOfVolunteer : listOfVolunteers) {
                        SendMessage sendMessage = new SendMessage(listOfVolunteer.getChatId(), "Усыновитель " + client.getName() + " : " + client.getChatId() + " более двух дней не отправлял отчет о питомце. Необходимо связаться с ним напрямую.");
                        telegramBot.execute(sendMessage);
                    }
                }
            }
        }
    }

    public void sendNoticesToVolunteersAboutEndOfProbationDates() {
        List<Client> listOfClientsWithAnimal = clientService.getListOfClientsWithAnimalAndProbationIsEnd();
        List<Volunteer> listOfVolunteers = volunteerService.getAllVolunteers();

        if (!listOfClientsWithAnimal.isEmpty()) {
            for (Client client : listOfClientsWithAnimal) {
                for (Volunteer listOfVolunteer : listOfVolunteers) {
                    InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton[]{});
                        SendMessage sendMessage = new SendMessage(listOfVolunteer.getChatId(), "У усыновителя " + client.getName() + " : " + client.getChatId() + " закончился испытательный срок. Необходимо принять решение воспользовавшись меню ниже.");
                        inlineKeyboard.addRow(new InlineKeyboardButton("Принять").callbackData("confirm_adoption" + client.getChatId().toString()),
                                new InlineKeyboardButton("Отклонить").callbackData("cancel_adoption" + client.getChatId().toString()),
                                new InlineKeyboardButton("Продлить ИС").callbackData("extend_probation" + client.getChatId().toString()));
                    sendMessage.replyMarkup(inlineKeyboard);
                        telegramBot.execute(sendMessage);
                }
            }
        }
    }

    @Override
    public Report getOlderReport(Message message) {
        Report report = reportRepository.getOlderReport();
        if (report != null) {
            reportRepository.markReportAsReviewed(report.getId(), message.chat().id());
            return report;
        }
        return null;
    }

    @Scheduled(cron = "0 0 0/2 * * *")
    public void sendNoticesToVolunteersAboutNewReports() {
        List<Volunteer> listOfVolunteers = volunteerService.getAllVolunteers();
        long chatId;
        if (reportRepository.getOlderReport() != null) {
            for (Volunteer listOfVolunteer : listOfVolunteers) {
                    chatId = listOfVolunteer.getChatId();
                    SendMessage request = new SendMessage(chatId, "Появились новые отчёты от усыновителей. Что бы ознакомиться нажмите кнопку ниже.");
                    request.replyMarkup(volunteerMenu);
                    telegramBot.execute(request);
            }
        }
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
