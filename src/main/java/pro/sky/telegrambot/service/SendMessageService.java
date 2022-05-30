package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static pro.sky.telegrambot.constant.BotMessageEnum.*;
import static pro.sky.telegrambot.constant.ButtonNameEnum.*;

@Service
public class SendMessageService {
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

    private final Logger logger = LoggerFactory.getLogger(SendMessageService.class);


    public SendMessage answerMessage(Message message) {
        logger.info("Answering message: {}", message);
        SendMessage msgForSend;
        Long chatId = message.chat().id();
        String name = message.from().firstName();
        String msg = message.text().trim();
        if (message.text() == null) {
            throw new IllegalArgumentException();
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
        } else {
            msgForSend = new SendMessage(chatId, NON_COMMAND_MESSAGE.getMessage());
            msgForSend.replyMarkup(mainMenu);
        }
        return msgForSend;
    }
}
