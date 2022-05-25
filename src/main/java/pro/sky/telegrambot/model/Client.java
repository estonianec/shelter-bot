package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Client {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long chatId; //Идентификатор чата

    String name; // Имя в ТГ
    String mail; // Контактная почта
    String phone; // Контактный телефон
    String contactName; // Контактное имя
    LocalDateTime adoptionDate; // Дата взятия животного
    LocalDateTime probationDate; // Дата окончания испытательного срока

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
        return Objects.equals(chatId, client.chatId) && Objects.equals(name, client.name) && Objects.equals(mail, client.mail) && Objects.equals(phone, client.phone) && Objects.equals(contactName, client.contactName) && Objects.equals(adoptionDate, client.adoptionDate) && Objects.equals(probationDate, client.probationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name, mail, phone, contactName, adoptionDate, probationDate);
    }
}
