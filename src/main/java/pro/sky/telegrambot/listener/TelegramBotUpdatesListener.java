package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.constant.ButtonNameEnum;

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

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null && update.message().text() != null) {
                chatId = update.message().chat().id();
                String name = update.message().from().firstName();
                String msg = update.message().text();
                SendMessage msgForSend;
                switch (msg) {
                    case "/start":
                        msgForSend = new SendMessage(chatId, "Добро пожаловать в наш бот. \uD83E\uDEE0");
                        msgForSend.replyMarkup(mainMenu);
                        telegramBot.execute(msgForSend);
                        break;
                    default:
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
