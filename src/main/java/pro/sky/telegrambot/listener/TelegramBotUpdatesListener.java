package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

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
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.message() != null && update.message().text() != null) {
                String msg = update.message().text();
                chatId = update.message().chat().id();
                String name = update.message().from().firstName();
                switch (msg) {
                    case "/start":
                        SendMessage msgForSend = new SendMessage(chatId, "Добро пожаловать в наш бот. \uD83E\uDEE0");
                        telegramBot.execute(msgForSend);
                        break;
                    default:
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
