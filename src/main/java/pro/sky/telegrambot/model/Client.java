package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Client {
    @Id
    Long chatId; //Идентификатор чата

    String firstName; // Имя в ТГ
    String phone; // Контактный телефон
    String lastName; // Контактное имя
    LocalDateTime adoptionDate; // Дата взятия животного
    LocalDateTime probationDate; // Дата окончания испытательного срока

    public Client(Long chatId, String firstName, String phone, String lastName, LocalDateTime adoptionDate, LocalDateTime probationDate) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.phone = phone;
        this.lastName = lastName;
        this.adoptionDate = adoptionDate;
        this.probationDate = probationDate;
    }

    public Client() {

    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String contactName) {
        this.lastName = contactName;
    }

    public LocalDateTime getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(LocalDateTime adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public LocalDateTime getProbationDate() {
        return probationDate;
    }

    public void setProbationDate(LocalDateTime probationDate) {
        this.probationDate = probationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(chatId, client.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
