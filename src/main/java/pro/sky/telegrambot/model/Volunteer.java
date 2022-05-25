package pro.sky.telegrambot.model;

import java.util.Objects;

public class Volunteer {

    long chatId;
    String userName;
    boolean isWorking;

    public long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return chatId == volunteer.chatId && isWorking == volunteer.isWorking && Objects.equals(userName, volunteer.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userName, isWorking);
    }
}
