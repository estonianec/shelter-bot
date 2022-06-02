package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.ReportRepository;
import pro.sky.telegrambot.service.ReportService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class ReportServiceImpl implements ReportService  {
    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    LocalDateTime penaltyTime;
    Report lastReport;

    private final ReportRepository reportRepository;
    private final ClientServiceImpl clientService;
    private final VolunteerServiceImpl volunteerService;
    @Autowired
    private TelegramBot telegramBot;

    public ReportServiceImpl(ReportRepository reportRepository, ClientServiceImpl clientService, VolunteerServiceImpl volunteerService) {
        this.reportRepository = reportRepository;
        this.clientService = clientService;
        this.volunteerService = volunteerService;
    }

    private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Scheduled(cron = "0 0 0 * * *")
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


}
