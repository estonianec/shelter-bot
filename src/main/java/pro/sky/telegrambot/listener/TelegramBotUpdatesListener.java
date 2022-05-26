package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

import static pro.sky.telegrambot.constant.ButtonNameEnum.*;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Long chatId;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
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

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null && update.message().text() != null) {
                chatId = update.message().chat().id();
                String name = update.message().from().firstName();
                String msg = update.message().text();
                SendMessage msgForSend;
                if (msg.equals("/start") || msg.equals(TO_MAIN_MENU.getButtonName())) {
                    msgForSend = new SendMessage(chatId, "Добро пожаловать в наш бот. \uD83E\uDEE0");
                    msgForSend.replyMarkup(mainMenu);
                    telegramBot.execute(msgForSend);
                } else if (msg.equals(SHELTER_INFO.getButtonName())) {
                    msgForSend = new SendMessage(chatId, "О чём конкретно вы хотели бы узнать? \uD83E\uDEE0");
                    msgForSend.replyMarkup(shelterInfoMenu);
                    telegramBot.execute(msgForSend);
                } else if (msg.equals(HOW_TO_TAKE_ANIMAL.getButtonName())) {
                    msgForSend = new SendMessage(chatId, "О чём конкретно вы хотели бы узнать? \uD83E\uDEE0");
                    msgForSend.replyMarkup(howToMenu);
                    telegramBot.execute(msgForSend);
                } else if (msg.equals(UPLOAD_REPORT.getButtonName())) {
                    msgForSend = new SendMessage(chatId, "О чём конкретно вы хотели бы узнать? \uD83E\uDEE0");
                    msgForSend.replyMarkup(adoptionMenu);
                    telegramBot.execute(msgForSend);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
