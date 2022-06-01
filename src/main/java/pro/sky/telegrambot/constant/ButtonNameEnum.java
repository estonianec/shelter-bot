package pro.sky.telegrambot.constant;

public enum ButtonNameEnum {
//    Главное меню
SHELTER_INFO("\uD83D\uDDFA Информация о приюте"),
    HOW_TO_TAKE_ANIMAL("\uD83D\uDC36 Как взять животное из приюта"),
    UPLOAD_REPORT("\uD83D\uDCF8 Загрузить отчет"),
    CALL_VOLUNTEER("\uD83D\uDE4B\u200D Позвать волонтера"),
    //    Меню о приюте
    SHELTER_HISTORY("История приюта"),
    SHELTER_CONTACT("Расписание работы приюта, адрес и схема проезда"),
    SHELTER_SECURITY("Общие рекомендации о технике безопасности на территории приюта"),
    GET_CONTACT("Предоставить контактные данные приюту"),
    UPLOAD_CONTACT("Отправить Ваши контактные данные"),
    TO_MAIN_MENU("\uD83D\uDD19 Вернуться в главное меню"),
    //    Меню советов и рекомендаций
    RULES_OF_ACQUAINTANCE("Правила знакомства с собакой"),
    REQUIRED_DOCUMENTS("Список необходимых документов"),
    REC_OF_TRANSPORTING("Рекомендации по транспортировке животного"),
    REC_HOME_PUPPY("Рекомендации по обустройству дома для щенка"),
    REC_HOME_ADULT_DOG("Рекомендаций по обустройству дома для взрослой собаки"),
    REC_HOME_DISABLED_DOG("Рекомендаций по обустройству дома для собаки-инвалида (зрение, передвижения)"),
    CYNOLOGIST_ADVICES("Советы кинолога по первичному общению с собакой"),
    LIST_OF_CYNOLOGISTS("Список проверенных кинологов"),
    REASONS_OF_DENY("Список причин отказа в заборе собаки из приюта"),
//    Меню усыновителя
    REPORT_FORM("Форма ежедневного отчета"),
    HOW_TO_SEND_REPORT("Как прислать отчёт"),
//    Меню волонтера
    GET_QUESTION("Получить самый ранний вопрос клиента"),
    GET_REPORT("Получить самый ранний отчёт усыновителя"),
    GET_LIST_OF_USERS_WITHOUT_ANIMAL("Получить список клиентов без животного");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}