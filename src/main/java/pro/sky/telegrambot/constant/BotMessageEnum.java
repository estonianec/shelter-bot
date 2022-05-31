package pro.sky.telegrambot.constant;

public enum BotMessageEnum {

    START_MESSAGE("Добро пожаловать в наш бот. \uD83E\uDEE0"),
    REQUEST_INFO_MESSAGE("О чём конкретно вы хотели бы узнать? \uD83E\uDEE0"),
    NON_COMMAND_MESSAGE("Пожалуйста, воспользуйтесь клавиатурой\uD83D\uDC47"),
    VOLUNTEER_MESSAGE("Привет, волонтер!"),
    //ответы на команды с клавиатуры,

    //ошибки
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так, обратитесь к разработчику");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
