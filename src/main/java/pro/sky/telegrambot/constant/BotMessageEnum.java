package pro.sky.telegrambot.constant;

public enum BotMessageEnum {

    START_MESSAGE("Добро пожаловать в наш бот. \uD83E\uDEE0"),
    REQUEST_INFO_MESSAGE("О чём конкретно вы хотели бы узнать? \uD83E\uDEE0"),
    NON_COMMAND_MESSAGE("Пожалуйста, воспользуйтесь меню\uD83D\uDC47"),
    VOLUNTEER_MESSAGE("Привет, волонтер!"),
    CLIENT_TO_VOLUNTEER_MESSAGE("В следующем сообщении отправьте пожалуйста Ваш вопрос волонтёру. "),
    CLIENT_TO_VOLUNTEER_MESSAGE_SAVE("Ваше сообщение получено и первый освободившийся волонтер Вам на него ответит."),
    //    Ответы на команды с клавиатуры

    //    Меню о приюте,
    SHELTER_HISTORY_MESSAGE("История приюта"),
    SHELTER_CONTACT_MESSAGE("Расписание работы приюта, адрес и схема проезда"),
    SHELTER_SECURITY_MESSAGE("Общие рекомендации о технике безопасности на территории приюта"),
    GET_CONTACT_MESSAGE("Просим Вас предоставить Ваши контактные данные приюту, воспользуйтесь клавиатурой\uD83D\uDC47"),
    SAVED_CONTACT_MESSAGE("Ваш контакт сохранен\uD83D\uDE4F"),

    //    Меню советов и рекомендаций
    RULES_OF_ACQUAINTANCE_MESSAGE("Правила знакомства с собакой"),
    REQUIRED_DOCUMENTS_MESSAGE("Список необходимых документов"),
    REC_OF_TRANSPORTING_MESSAGE("Рекомендации по транспортировке животного"),
    REC_HOME_PUPPY_MESSAGE("Рекомендации по обустройству дома для щенка"),
    REC_HOME_ADULT_DOG_MESSAGE("Рекомендаций по обустройству дома для взрослой собаки"),
    REC_HOME_DISABLED_DOG_MESSAGE("Рекомендаций по обустройству дома для собаки-инвалида (зрение, передвижения)"),
    CYNOLOGIST_ADVICES_MESSAGE("Советы кинолога по первичному общению с собакой"),
    LIST_OF_CYNOLOGISTS_MESSAGE("Список проверенных кинологов"),
    REASONS_OF_DENY_MESSAGE("Список причин отказа в заборе собаки из приюта"),

    //    Меню усыновителя
    REPORT_FORM_MESSAGE("Форма ежедневного отчета"),
    HOW_TO_SEND_REPORT_MESSAGE("Как прислать отчёт"),

    //    Меню волонтера
    OPEN_JOB_MESSAGE("С настоящего момента вам начнут приходить уведомления о новых сообщениях."),
    CLOSE_JOB_MESSAGE("С настоящего момента вам перестанут приходить уведомления о новых сообщениях."),

    //    Ошибки
    NO_CLIENTS_WITHOUT_ANIMALS("Клиентов без животных больше не осталось. Вложитесь в рекламу."),
    EXCEPTION_WHAT_THE_FUCK("Что-то пошло не так, обратитесь к разработчику");

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    }
