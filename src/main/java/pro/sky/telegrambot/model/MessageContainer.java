package pro.sky.telegrambot.model;

import com.pengrad.telegrambot.model.request.Keyboard;

import java.util.Objects;

public class MessageContainer {
    private String message;
    private Keyboard keyboard;

    public MessageContainer(String message, Keyboard keyboard) {
        this.message = message;
        this.keyboard = keyboard;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Keyboard getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageContainer that = (MessageContainer) o;
        return Objects.equals(message, that.message) && Objects.equals(keyboard, that.keyboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, keyboard);
    }
}
