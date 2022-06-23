package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exceptions.IllegalMessageException;
import pro.sky.telegrambot.service.impl.SendMessageServiceImpl;

import javax.annotation.PostConstruct;
import java.util.List;

import static pro.sky.telegrambot.constant.Constants.EXCEPTION_WHAT_THE_FUCK;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private SendMessageServiceImpl sendMessageService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {


        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
                handleUpdate(update);
            } catch (IllegalMessageException e) {
                telegramBot.execute(new SendMessage(update.message().chat().id(),
                        EXCEPTION_WHAT_THE_FUCK));
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(Update update) {
        if (update.callbackQuery() != null) {
            telegramBot.execute(sendMessageService.answerMessage(update));
        } else if (update.message() != null) {
            telegramBot.execute(sendMessageService.answerMessage(update.message()));
        }
    }

}
