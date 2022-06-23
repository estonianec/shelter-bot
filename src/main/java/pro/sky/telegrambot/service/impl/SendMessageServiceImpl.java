package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exceptions.IllegalMessageException;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Question;
import pro.sky.telegrambot.model.Report;
import pro.sky.telegrambot.service.*;

import java.util.List;

import static pro.sky.telegrambot.constant.Constants.*;

@Service

public class SendMessageServiceImpl implements SendMessageService {

    private final VolunteerService volunteerService;
    private final ClientService clientService;
    private final QuestionService questionService;
    private final ReportService reportService;
    @Autowired
    private TelegramBot telegramBot;

    public SendMessageServiceImpl(VolunteerService volunteerService, ClientService clientService, QuestionService questionService, ReportService reportService) {
        this.volunteerService = volunteerService;
        this.clientService = clientService;
        this.questionService = questionService;
        this.reportService = reportService;
    }

    //        Стартовое меню
    Keyboard mainMenu = new ReplyKeyboardMarkup(
            new String[]{SHELTER_INFO, HOW_TO_TAKE_ANIMAL},
            new String[]{UPLOAD_REPORT, CALL_VOLUNTEER})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню о приюте
    Keyboard shelterInfoMenu = new ReplyKeyboardMarkup(
            new String[]{SHELTER_HISTORY, SHELTER_CONTACT},
            new String[]{SHELTER_SECURITY, GET_CONTACT},
            new String[]{CALL_VOLUNTEER, TO_MAIN_MENU})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню советов и рекомендаций
    Keyboard howToMenu = new ReplyKeyboardMarkup(
            new String[]{RULES_OF_ACQUAINTANCE, REQUIRED_DOCUMENTS},
            new String[]{REC_OF_TRANSPORTING, REC_HOME_PUPPY},
            new String[]{REC_HOME_ADULT_DOG, REC_HOME_DISABLED_DOG},
            new String[]{CYNOLOGIST_ADVICES, LIST_OF_CYNOLOGISTS},
            new String[]{REASONS_OF_DENY, GET_CONTACT},
            new String[]{CALL_VOLUNTEER, TO_MAIN_MENU})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню усыновителя
    Keyboard adoptionMenu = new ReplyKeyboardMarkup(
            new String[]{REPORT_FORM, HOW_TO_SEND_REPORT},
            new String[]{CALL_VOLUNTEER, TO_MAIN_MENU})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //      Меню волонтера
    Keyboard volunteerMenu = new ReplyKeyboardMarkup(
            new String[]{GET_QUESTION, GET_REPORT},
            new String[]{OPEN_JOB, CLOSE_JOB},
            new String[]{GET_LIST_OF_USERS})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //      Меню контакта
    Keyboard contactMenu = new ReplyKeyboardMarkup(
            new KeyboardButton(UPLOAD_CONTACT).requestContact(true),
            new KeyboardButton(TO_MAIN_MENU))
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);

    private final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);


    @Override
    public SendMessage answerMessage(Message message) {
        logger.info("Answering message: {}", message);
        SendMessage msgForSend = null;
        Long chatId = message.chat().id();
        if (message.text() != null) {
            msgForSend = getSendMessageFromText(message);
        }
        if (message.text() == null && message.contact() != null) {
            logger.info("Saving contact {}", message.contact());
            clientService.insertContact(message);
            msgForSend = new SendMessage(chatId, SAVED_CONTACT_MESSAGE);
            msgForSend.replyMarkup(shelterInfoMenu);
        }
        if (message.text() == null && message.photo() != null && message.caption() != null) {
            if (clientService.getClientByChatId(chatId).getAdoptionStatus() == 1) {
                reportService.saveReport(message);
                msgForSend = new SendMessage(chatId, REPORT_SAVED_MESSAGE);
                msgForSend.replyMarkup(adoptionMenu);
            } else {
                msgForSend = new SendMessage(chatId, REPORT_NO_NEEDED_MESSAGE);
                msgForSend.replyMarkup(howToMenu);
            }
        }
        if (message.text() == null && message.photo() != null && message.caption() == null) {
            msgForSend = new SendMessage(chatId, REPORT_WITHOUT_DESCRIPTION_MESSAGE +
                    "\n" + REPORT_FORM_MESSAGE);
            msgForSend.replyMarkup(adoptionMenu);
        }
        if (message.text() == null && message.contact() == null && message.photo().length == 0) {
            throw new IllegalMessageException();
        }
        return msgForSend;
    }

    @Override
    public SendMessage answerMessage(Update update) {
        SendMessage msgForSend;
        Long chatId = update.callbackQuery().from().id();
        String dataWithoutCommand;
        long clientChatId;
        if (update.callbackQuery().data().startsWith("give_animal_to_user")) {
            dataWithoutCommand = update.callbackQuery().data().replace("give_animal_to_user", "");
            clientChatId = Long.parseLong(dataWithoutCommand);
            clientService.setAdoptionDate(clientChatId);
            clientService.setProbationDate(clientChatId);
            msgForSend = new SendMessage(chatId, "Клиент " + clientChatId + " получил животное.");
            msgForSend.replyMarkup(volunteerMenu);
        } else if (update.callbackQuery().data().startsWith("send_note")) {
                dataWithoutCommand = update.callbackQuery().data().replace("send_note", "");
                clientChatId = Long.parseLong(dataWithoutCommand);
                msgForSend = new SendMessage(chatId, "Усыновителю " + clientChatId + " отправлено сообщение о некачественном отчёте.");
                msgForSend.replyMarkup(volunteerMenu);
                telegramBot.execute(new SendMessage(clientChatId, "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания собаки"));
        } else if (update.callbackQuery().data().startsWith("confirm_adoption")) {
            dataWithoutCommand = update.callbackQuery().data().replace("confirm_adoption", "");
            clientChatId = Long.parseLong(dataWithoutCommand);
            msgForSend = new SendMessage(chatId, "Усыновитель " + clientChatId + " успешно прошёл испытательный срок.");
            msgForSend.replyMarkup(volunteerMenu);
            clientService.setAdoptionStatus(2, clientChatId);
            telegramBot.execute(new SendMessage(clientChatId, "Поздравляем Вас с успешным окончанием испытательного срока!"));
        } else if (update.callbackQuery().data().startsWith("cancel_adoption")) {
            dataWithoutCommand = update.callbackQuery().data().replace("cancel_adoption", "");
            clientChatId = Long.parseLong(dataWithoutCommand);
            msgForSend = new SendMessage(chatId, "Усыновитель " + clientChatId + " не прошёл испытательный срок. Инструкция по дальнейшим действиям отправлена усыновителю");
            msgForSend.replyMarkup(volunteerMenu);
            clientService.setAdoptionStatus(3, clientChatId);
            telegramBot.execute(new SendMessage(clientChatId, "К сожалению, Вы не прошли испытательный срок, и мы вынуждены отозвать животное. В ближайшее время с Вами свяжется наш волонтер"));
        } else if (update.callbackQuery().data().startsWith("add_probation_days")) {
            dataWithoutCommand = update.callbackQuery().data().replace("add_probation_days", "");
            int countOfDays = Integer.parseInt(dataWithoutCommand.substring(0, dataWithoutCommand.indexOf('.')));
            clientChatId = Long.parseLong(dataWithoutCommand.substring(dataWithoutCommand.indexOf('.') + 1));
            clientService.addProbationDays(countOfDays, clientChatId);
            msgForSend = new SendMessage(chatId, "Усыновителю " + clientChatId + " продлен испытательный срок на " + countOfDays);
            msgForSend.replyMarkup(volunteerMenu);
            telegramBot.execute(new SendMessage(clientChatId, "Ваш испытательный срок продлен на " + countOfDays + " дней"));
        } else if (update.callbackQuery().data().startsWith("extend_probation")) {
            dataWithoutCommand = update.callbackQuery().data().replace("extend_probation", "");
            clientChatId = Long.parseLong(dataWithoutCommand);
            msgForSend = new SendMessage(chatId, "Усыновителю " + clientChatId + " будет установлен новый испытательный срок. Выберите из меню, какое кол-во дней добавить к испытательному сроку усыновителя.");
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{});
            int day = 0;
            for (int i = 0; i < 10; i++) {
                inlineKeyboard.addRow(new InlineKeyboardButton(day + 1 + "").callbackData("add_probation_days" + (day + 1) + "." + clientChatId),
                        new InlineKeyboardButton(day + 2 + "").callbackData("add_probation_days" + (day + 2) + "." + clientChatId),
                        new InlineKeyboardButton(day + 3 + "").callbackData("add_probation_days" + (day + 3) + "." + clientChatId),
                        new InlineKeyboardButton(day + 4 + "").callbackData("add_probation_days" + (day + 4) + "." + clientChatId),
                        new InlineKeyboardButton(day + 5 + "").callbackData("add_probation_days" + (day + 5) + "." + clientChatId),
                        new InlineKeyboardButton(day + 6 + "").callbackData("add_probation_days" + (day + 6) + "." + clientChatId),
                        new InlineKeyboardButton(day + 7 + "").callbackData("add_probation_days" + (day + 7) + "." + clientChatId),
                        new InlineKeyboardButton(day + 8 + "").callbackData("add_probation_days" + (day + 8) + "." + clientChatId));
                day += 8;
            }
            msgForSend.replyMarkup(inlineKeyboard);
        } else throw new IllegalArgumentException();

        return msgForSend;
    }

    private SendMessage getSendMessageFromText(Message message) {
        SendMessage msgForSend;
        String msg = message.text().trim();
        Long chatId = message.chat().id();
        if (!clientService.isClientExists(chatId)) {
            clientService.createNewClient(message);
            msgForSend = new SendMessage(chatId, "Добрый день, " + message.from().firstName() + "! Рады приветствовать тебя в нашем приюте %shelter_name%!");
            msgForSend.replyMarkup(mainMenu);
        } else if (msg.equals("/start") || msg.equals(TO_MAIN_MENU)) {
            msgForSend = new SendMessage(chatId, START_MESSAGE);
            msgForSend.replyMarkup(mainMenu);
        } else if (msg.equals(SHELTER_INFO)) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE);
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(HOW_TO_TAKE_ANIMAL)) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(UPLOAD_REPORT)) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE);
            msgForSend.replyMarkup(adoptionMenu);

            //    Меню о приюте
        } else if (msg.equals(SHELTER_HISTORY)) {
            msgForSend = new SendMessage(chatId, SHELTER_HISTORY_MESSAGE);
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(SHELTER_CONTACT)) {
            msgForSend = new SendMessage(chatId, SHELTER_CONTACT_MESSAGE);
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(SHELTER_SECURITY)) {
            msgForSend = new SendMessage(chatId, SHELTER_SECURITY_MESSAGE);
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(GET_CONTACT)) {
            msgForSend = new SendMessage(chatId, GET_CONTACT_MESSAGE);
            msgForSend.replyMarkup(contactMenu);

            //    Меню советов и рекомендаций
        } else if (msg.equals(RULES_OF_ACQUAINTANCE)) {
            msgForSend = new SendMessage(chatId, RULES_OF_ACQUAINTANCE_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REQUIRED_DOCUMENTS)) {
            msgForSend = new SendMessage(chatId, REQUIRED_DOCUMENTS_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_OF_TRANSPORTING)) {
            msgForSend = new SendMessage(chatId, REC_OF_TRANSPORTING_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_PUPPY)) {
            msgForSend = new SendMessage(chatId, REC_HOME_PUPPY_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_ADULT_DOG)) {
            msgForSend = new SendMessage(chatId, REC_HOME_ADULT_DOG_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_DISABLED_DOG)) {
            msgForSend = new SendMessage(chatId, REC_HOME_DISABLED_DOG_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(CYNOLOGIST_ADVICES)) {
            msgForSend = new SendMessage(chatId, CYNOLOGIST_ADVICES_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(LIST_OF_CYNOLOGISTS)) {
            msgForSend = new SendMessage(chatId, LIST_OF_CYNOLOGISTS_MESSAGE);
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REASONS_OF_DENY)) {
            msgForSend = new SendMessage(chatId, REASONS_OF_DENY_MESSAGE);
            msgForSend.replyMarkup(howToMenu);

            //    Меню усыновителя
        } else if (msg.equals(REPORT_FORM)) {
            msgForSend = new SendMessage(chatId, REPORT_FORM_MESSAGE);
            msgForSend.replyMarkup(adoptionMenu);
        } else if (msg.equals(HOW_TO_SEND_REPORT)) {
            msgForSend = new SendMessage(chatId, HOW_TO_SEND_REPORT_MESSAGE);
            msgForSend.replyMarkup(adoptionMenu);

            //    Меню волонтера
        } else if (msg.equals(GET_QUESTION) && (volunteerService.isVolunteerExists(chatId))) {
            Question question = questionService.getOlderQuestion(message);
            if (question != null) {
                msgForSend = new SendMessage(chatId, "Поступил вопрос от клиента:\n" + question.getQuestion() + "\nВаше следующее сообщение станет ответом на вопрос. Будьте внимательны!");
            } else {
                msgForSend = new SendMessage(chatId, "Вопросов без ответа не осталось.");
                msgForSend.replyMarkup(volunteerMenu);
            }
        } else if (msg.equals(GET_REPORT) && (volunteerService.isVolunteerExists(chatId))) {
            Report report = reportService.getOlderReport(message);
            if (report != null) {
                telegramBot.execute(new SendPhoto(chatId, report.getFileId()));
                telegramBot.execute(new SendMessage(chatId, report.getDescription()));
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{});
                inlineKeyboard.addRow(new InlineKeyboardButton("Отправить замечание").callbackData("send_note" + report.getClient().getChatId()));
                msgForSend = new SendMessage(chatId, "Отчёт от пользователя " + report.getClient().getName() + " уже помечен, как просмотренный. Если Вас не устраивает качество отчёта, нажмите кнопку \"Отправить замечание\" ниже.");
                msgForSend.replyMarkup(inlineKeyboard);
            } else {
                msgForSend = new SendMessage(chatId, "Непросмотренных отчетов не осталось.");
                msgForSend.replyMarkup(volunteerMenu);
            }
        } else if (msg.equals("/volunteer") && (volunteerService.isVolunteerExists(chatId))) {
            msgForSend = new SendMessage(chatId, VOLUNTEER_MESSAGE);
            msgForSend.replyMarkup(volunteerMenu);
        } else if (msg.equals(OPEN_JOB) && (volunteerService.isVolunteerExists(chatId))) {
            msgForSend = new SendMessage(chatId, OPEN_JOB_MESSAGE);
            volunteerService.openJob(message.chat().id());
            msgForSend.replyMarkup(volunteerMenu);
        } else if (msg.equals(CLOSE_JOB) && (volunteerService.isVolunteerExists(chatId))) {
            msgForSend = new SendMessage(chatId, CLOSE_JOB_MESSAGE);
            volunteerService.closeJob(message.chat().id());
            msgForSend.replyMarkup(volunteerMenu);

        } else if (msg.equals(GET_LIST_OF_USERS) && (volunteerService.isVolunteerExists(chatId))) {
            List<Client> listOfClients = clientService.getListOfUsersWithoutAnimal();
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{});
            if (!listOfClients.isEmpty()) {
                StringBuilder clients = new StringBuilder("Name : chatId\n");
                for (Client listOfClient : listOfClients) {
                    clients.append(listOfClient.getName()).append(" : ").append(listOfClient.getChatId()).append("\n");
                    inlineKeyboard.addRow(new InlineKeyboardButton(listOfClient.getName()).callbackData("give_animal_to_user" + listOfClient.getChatId().toString()));
                }
                clients.append("\nДля выбора клиента, которому будет предоставлено животное, нажмите на кнопку с его именем снизу");
                msgForSend = new SendMessage(chatId, clients.toString());
                msgForSend.replyMarkup(inlineKeyboard);
            } else {
                msgForSend = new SendMessage(chatId, NO_CLIENTS_WITHOUT_ANIMALS);
                msgForSend.replyMarkup(volunteerMenu);
            }


        } else if ((volunteerService.isVolunteerExists(chatId)) && questionService.isItAnswer(message)) {
            Question question = questionService.makeAnswer(message);
            msgForSend = new SendMessage(question.getChatId(), "Добрый день!\n\n" + message.text());
            msgForSend.replyMarkup(mainMenu);

            //     Вопрос волонтеру
        } else if (msg.equals(CALL_VOLUNTEER)) {
            msgForSend = new SendMessage(chatId, CLIENT_TO_VOLUNTEER_MESSAGE);
            questionService.createEmptyQuestion(message);
        } else if (questionService.isQuestionExist(chatId)) {
            msgForSend = new SendMessage(chatId, CLIENT_TO_VOLUNTEER_MESSAGE_SAVE);
            questionService.createNewQuestion(message);
            msgForSend.replyMarkup(mainMenu);
        } else {
            msgForSend = new SendMessage(chatId, NON_COMMAND_MESSAGE);
            msgForSend.replyMarkup(mainMenu);
        }
        return msgForSend;
    }
}
