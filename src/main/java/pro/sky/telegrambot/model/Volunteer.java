package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Volunteer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long chatId; // Идентификатор чата

    String userName; // Имя волонтёра из ТГ
    boolean isWorking; // Статус работы волонтёра

    public Volunteer(long chatId, String userName, boolean isWorking) {
        this.chatId = chatId;
        this.userName = userName;
        this.isWorking = isWorking;
    }

    public Volunteer() {

    }

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
