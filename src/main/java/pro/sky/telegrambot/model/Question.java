package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Question {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    Long chatId;
    String question;
    Boolean isAnswered;
    @ManyToOne
    Volunteer volunteer;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", question='" + question + '\'' +
                ", isAnswered=" + isAnswered +
                ", volunteer=" + volunteer +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public Question() {
    }

    public Question(Long id, Long chatId, String question, Boolean isAnswered, Volunteer volunteer) {
        this.id = id;
        this.chatId = chatId;
        this.question = question;
        this.isAnswered = isAnswered;
        this.volunteer = volunteer;
    }

    @ManyToOne(optional = false)
    private Volunteer volunteers;

    public Volunteer getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(Volunteer volunteers) {
        this.volunteers = volunteers;
    }
}
