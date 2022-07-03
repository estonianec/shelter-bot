package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import pro.sky.telegrambot.model.Client;

import java.util.Collection;
import java.util.List;

public interface ClientService {
    boolean isClientExists(Long chatId);

    void createNewClient(Message message);

    List<Client> getListOfUsersWithoutAnimal();

    void setAdoptionDate(Long chatId);

    void setProbationDate(Long chatId);

    void insertContact(Message message);

    List<Client> getListOfClientsWithAnimal();

    List<Client> getListOfClientsWithAnimalAndProbationIsEnd();

    void setAdoptionStatus(int i, Long clientChatId);

    void addProbationDays(int countOfDays, Long clientChatId);

    Client getClientByChatId(Long clientChatId);

    Collection<Client> getAllClients();

    Client editClient(Client client);

    void deleteClient(long id);

    Client createClient(Client client);
}
