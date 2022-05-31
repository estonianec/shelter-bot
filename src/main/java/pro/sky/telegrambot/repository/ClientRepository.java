package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getClientByChatId(Long chatId);
}
