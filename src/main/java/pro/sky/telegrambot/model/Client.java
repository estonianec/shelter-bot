package pro.sky.telegrambot.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Client {
    Long chatId;
    String name;
    String mail;
    String phone;
    String contactName;
    LocalDateTime adoptionDate;
    LocalDateTime probationDate;

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
