package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.ClientService;
import pro.sky.telegrambot.service.SendMessageService;
import pro.sky.telegrambot.service.VolunteerService;

import static pro.sky.telegrambot.constant.BotMessageEnum.*;
import static pro.sky.telegrambot.constant.ButtonNameEnum.*;

@Service
public class SendMessageServiceImpl implements SendMessageService {

    private final VolunteerService volunteerService;
    private final ClientService clientService;

    public SendMessageServiceImpl(VolunteerService volunteerService, ClientService clientService) {
        this.volunteerService = volunteerService;
        this.clientService = clientService;
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

    Keyboard volunteerMenu = new ReplyKeyboardMarkup(
            new String[]{GET_QUESTION.getButtonName(), GET_REPORT.getButtonName()},
            new String[]{GET_LIST_OF_USERS_WITHOUT_ANIMAL.getButtonName()})
            .resizeKeyboard(true)
            .oneTimeKeyboard(true)
            .selective(true);

    private final Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);


    @Override
    public SendMessage answerMessage(Message message) {
        logger.info("Answering message: {}", message);
        SendMessage msgForSend;
        Long chatId = message.chat().id();
        String name = message.from().firstName();
        String msg = message.text().trim();
        if (message.text() == null) {
            throw new IllegalArgumentException();
        } else if (!clientService.isClientExists(chatId)) {
            clientService.createNewClient(message);
            msgForSend = new SendMessage(chatId, "Добрый день, " + name + "! Рады приветствовать тебя в нашем приюте %shelter_name%!");
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
            msgForSend.replyMarkup(shelterInfoMenu);

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

        } else if (msg.equals("/volunteer") && (volunteerService.isVolunteerExists(chatId))){
            msgForSend = new SendMessage(chatId, VOLUNTEER_MESSAGE.getMessage());
            msgForSend.replyMarkup(volunteerMenu);
        } else {
            msgForSend = new SendMessage(chatId, NON_COMMAND_MESSAGE.getMessage());
            msgForSend.replyMarkup(mainMenu);
        }
        return msgForSend;
    }
}
