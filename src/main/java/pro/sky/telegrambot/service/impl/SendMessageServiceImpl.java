package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.model.Question;
import pro.sky.telegrambot.service.ClientService;
import pro.sky.telegrambot.service.QuestionService;
import pro.sky.telegrambot.service.SendMessageService;
import pro.sky.telegrambot.service.VolunteerService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static pro.sky.telegrambot.constant.BotMessageEnum.*;
import static pro.sky.telegrambot.constant.ButtonNameEnum.*;

@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final VolunteerService volunteerService;
    private final ClientService clientService;
    private final QuestionService questionService;

    public SendMessageServiceImpl(VolunteerService volunteerService, ClientService clientService, QuestionService questionService) {
        this.volunteerService = volunteerService;
        this.clientService = clientService;
        this.questionService = questionService;
    }

    //        Стартовое меню
    Keyboard mainMenu = new ReplyKeyboardMarkup(
            new String[]{SHELTER_INFO.getButtonName(), HOW_TO_TAKE_ANIMAL.getButtonName()},
            new String[]{UPLOAD_REPORT.getButtonName(), CALL_VOLUNTEER.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню о приюте
    Keyboard shelterInfoMenu = new ReplyKeyboardMarkup(
            new String[]{SHELTER_HISTORY.getButtonName(), SHELTER_CONTACT.getButtonName()},
            new String[]{SHELTER_SECURITY.getButtonName(), GET_CONTACT.getButtonName()},
            new String[]{CALL_VOLUNTEER.getButtonName(), TO_MAIN_MENU.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню советов и рекомендаций
    Keyboard howToMenu = new ReplyKeyboardMarkup(
            new String[]{RULES_OF_ACQUAINTANCE.getButtonName(), REQUIRED_DOCUMENTS.getButtonName()},
            new String[]{REC_OF_TRANSPORTING.getButtonName(), REC_HOME_PUPPY.getButtonName()},
            new String[]{REC_HOME_ADULT_DOG.getButtonName(), REC_HOME_DISABLED_DOG.getButtonName()},
            new String[]{CYNOLOGIST_ADVICES.getButtonName(), LIST_OF_CYNOLOGISTS.getButtonName()},
            new String[]{REASONS_OF_DENY.getButtonName(), GET_CONTACT.getButtonName()},
            new String[]{CALL_VOLUNTEER.getButtonName(), TO_MAIN_MENU.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //        Меню усыновителя
    Keyboard adoptionMenu = new ReplyKeyboardMarkup(
            new String[]{REPORT_FORM.getButtonName(), HOW_TO_SEND_REPORT.getButtonName()},
            new String[]{CALL_VOLUNTEER.getButtonName(), TO_MAIN_MENU.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //      Меню волонтера
    Keyboard volunteerMenu = new ReplyKeyboardMarkup(
            new String[]{GET_QUESTION.getButtonName(), GET_REPORT.getButtonName()},
            new String[]{OPEN_JOB.getButtonName(), CLOSE_JOB.getButtonName()},
            new String[]{GET_LIST_OF_USERS.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);
    //      Меню контакта
    Keyboard contactMenu = new ReplyKeyboardMarkup(
            new KeyboardButton(UPLOAD_CONTACT.getButtonName()).requestContact(true),
            new KeyboardButton(TO_MAIN_MENU.getButtonName()))
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
            clientService.createNewClient(message);
            msgForSend = new SendMessage(chatId, SAVED_CONTACT_MESSAGE.getMessage());
            msgForSend.replyMarkup(shelterInfoMenu);
        }
        if (message.text() == null && message.contact() == null) {
            throw new IllegalArgumentException();
        }
        return msgForSend;
    }

    @Override
    public SendMessage answerMessage(Update update) {
        SendMessage msgForSend;
        Long chatId = update.callbackQuery().from().id();
        String dataWithoutCommand = ""; //нужна для удаления других команд из chatId в дальнейшем
        String client = null;
        if (update.callbackQuery().data().startsWith("give_animal_to_user")) {
            dataWithoutCommand = update.callbackQuery().data().replace("give_animal_to_user", "");
            List<Client> listOfClients = clientService.getListOfUsersWithoutAnimal();
            for (Client listOfClient : listOfClients) {
                if (dataWithoutCommand.equals(listOfClient.getChatId().toString())) {
                    clientService.setAdoptionDate(listOfClient.getChatId());
                    clientService.setProbationDate(listOfClient.getChatId());
                    client = listOfClient.getName();
                }
            }
        }
        msgForSend = new SendMessage(chatId, "Клиент " + client + " получил животное.");
            msgForSend.replyMarkup(volunteerMenu);

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
        } else if (msg.equals("/start") || msg.equals(TO_MAIN_MENU.getButtonName())) {
            msgForSend = new SendMessage(chatId, START_MESSAGE.getMessage());
            msgForSend.replyMarkup(mainMenu);
        } else if (msg.equals(SHELTER_INFO.getButtonName())) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE.getMessage());
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(HOW_TO_TAKE_ANIMAL.getButtonName())) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(UPLOAD_REPORT.getButtonName())) {
            msgForSend = new SendMessage(chatId, REQUEST_INFO_MESSAGE.getMessage());
            msgForSend.replyMarkup(adoptionMenu);

            //    Меню о приюте
        } else if (msg.equals(SHELTER_HISTORY.getButtonName())) {
            msgForSend = new SendMessage(chatId, SHELTER_HISTORY_MESSAGE.getMessage());
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(SHELTER_CONTACT.getButtonName())) {
            msgForSend = new SendMessage(chatId, SHELTER_CONTACT_MESSAGE.getMessage());
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(SHELTER_SECURITY.getButtonName())) {
            msgForSend = new SendMessage(chatId, SHELTER_SECURITY_MESSAGE.getMessage());
            msgForSend.replyMarkup(shelterInfoMenu);
        } else if (msg.equals(GET_CONTACT.getButtonName())) {
            msgForSend = new SendMessage(chatId, GET_CONTACT_MESSAGE.getMessage());
            msgForSend.replyMarkup(contactMenu);

            //    Меню советов и рекомендаций
        } else if (msg.equals(RULES_OF_ACQUAINTANCE.getButtonName())) {
            msgForSend = new SendMessage(chatId, RULES_OF_ACQUAINTANCE_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REQUIRED_DOCUMENTS.getButtonName())) {
            msgForSend = new SendMessage(chatId, REQUIRED_DOCUMENTS_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_OF_TRANSPORTING.getButtonName())) {
            msgForSend = new SendMessage(chatId, REC_OF_TRANSPORTING_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_PUPPY.getButtonName())) {
            msgForSend = new SendMessage(chatId, REC_HOME_PUPPY_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_ADULT_DOG.getButtonName())) {
            msgForSend = new SendMessage(chatId, REC_HOME_ADULT_DOG_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REC_HOME_DISABLED_DOG.getButtonName())) {
            msgForSend = new SendMessage(chatId, REC_HOME_DISABLED_DOG_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(CYNOLOGIST_ADVICES.getButtonName())) {
            msgForSend = new SendMessage(chatId, CYNOLOGIST_ADVICES_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(LIST_OF_CYNOLOGISTS.getButtonName())) {
            msgForSend = new SendMessage(chatId, LIST_OF_CYNOLOGISTS_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);
        } else if (msg.equals(REASONS_OF_DENY.getButtonName())) {
            msgForSend = new SendMessage(chatId, REASONS_OF_DENY_MESSAGE.getMessage());
            msgForSend.replyMarkup(howToMenu);

            //    Меню усыновителя
        } else if (msg.equals(REPORT_FORM.getButtonName())) {
            msgForSend = new SendMessage(chatId, REPORT_FORM_MESSAGE.getMessage());
            msgForSend.replyMarkup(adoptionMenu);
        } else if (msg.equals(HOW_TO_SEND_REPORT.getButtonName())) {
            msgForSend = new SendMessage(chatId, HOW_TO_SEND_REPORT_MESSAGE.getMessage());
            msgForSend.replyMarkup(adoptionMenu);

            //    Меню волонтера
        } else if (msg.equals(GET_QUESTION.getButtonName()) && (volunteerService.isVolunteerExists(chatId))){
            Question question = questionService.getOlderQuestion(message);
            if (question != null) {
                msgForSend = new SendMessage(chatId, "Поступил вопрос от клиента:\n" + question.getQuestion() + "\nВаше следующее сообщение станет ответом на вопрос. Будьте внимательны!");
            } else {
                msgForSend = new SendMessage(chatId, "Вопросов без ответа не осталось.");
                msgForSend.replyMarkup(volunteerMenu);
            }
        } else if (msg.equals("/volunteer") && (volunteerService.isVolunteerExists(chatId))){
            msgForSend = new SendMessage(chatId, VOLUNTEER_MESSAGE.getMessage());
            msgForSend.replyMarkup(volunteerMenu);
        } else if (msg.equals(OPEN_JOB.getButtonName()) && (volunteerService.isVolunteerExists(chatId))){
            msgForSend = new SendMessage(chatId, OPEN_JOB_MESSAGE.getMessage());
            volunteerService.openJob(message.chat().id());
            msgForSend.replyMarkup(volunteerMenu);
        } else if (msg.equals(CLOSE_JOB.getButtonName()) && (volunteerService.isVolunteerExists(chatId))){
            msgForSend = new SendMessage(chatId, CLOSE_JOB_MESSAGE.getMessage());
            volunteerService.closeJob(message.chat().id());
            msgForSend.replyMarkup(volunteerMenu);

        } else if (msg.equals(GET_LIST_OF_USERS.getButtonName()) && (volunteerService.isVolunteerExists(chatId))){
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
                msgForSend = new SendMessage(chatId, NO_CLIENTS_WITHOUT_ANIMALS.getMessage());
                msgForSend.replyMarkup(volunteerMenu);
            }


        } else if ((volunteerService.isVolunteerExists(chatId)) && questionService.isItAnswer(message)){
            Question question = questionService.makeAnswer(message);
            msgForSend = new SendMessage(question.getChatId(), "Добрый день!\n\n" + message.text());
            msgForSend.replyMarkup(mainMenu);

            //     Вопрос волонтеру
        } else if (msg.equals(CALL_VOLUNTEER.getButtonName())){
            msgForSend = new SendMessage(chatId, CLIENT_TO_VOLUNTEER_MESSAGE.getMessage());
            questionService.createEmptyQuestion(message);
        } else if (questionService.isQuestionExist(chatId)){
            msgForSend = new SendMessage(chatId, CLIENT_TO_VOLUNTEER_MESSAGE_SAVE.getMessage());
            questionService.createNewQuestion(message);
            msgForSend.replyMarkup(mainMenu);
        } else {
            msgForSend = new SendMessage(chatId, NON_COMMAND_MESSAGE.getMessage());
            msgForSend.replyMarkup(mainMenu);
        }
        return msgForSend;
    }
}
