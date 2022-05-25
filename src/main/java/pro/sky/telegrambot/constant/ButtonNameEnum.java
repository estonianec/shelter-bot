package pro.sky.telegrambot.constant;

public enum ButtonNameEnum {
    SHELTER_INFO("\uD83D\uDDFA Информация о приюте"),
    HOW_TO_TAKE_ANIMAL("\uD83D\uDC36 Как взять животное из приюта"),
    UPLOAD_REPORT("\uD83D\uDCF8 Загрузить отчет"),
    CALL_VOLUNTEER("\uD83D\uDE4B\u200D Позвать волонтера");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}