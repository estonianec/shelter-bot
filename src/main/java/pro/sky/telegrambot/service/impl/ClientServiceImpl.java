package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.ClientService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public boolean isClientExists(Long chatId) {
        return clientRepository.getClientByChatId(chatId) != null;
    }

    @Override
    public void createNewClient(Message message) {
        Client client = new Client();
        client.setChatId(message.chat().id());
        client.setName(message.from().firstName());
        client.setAdoptionStatus(0);
        clientRepository.save(client);
    }

    @Override
    public List<Client> getListOfUsersWithoutAnimal() {
        return clientRepository.getClientsByAdoptionDateIsNull();
    }

    @Override
    public void setAdoptionDate(Long chatId) {
        clientRepository.setAdoptionDate(currentTime, chatId);
    }

    @Override
    public void setProbationDate(Long chatId) {
        LocalDateTime newTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        newTime = newTime.plusDays(30);
        clientRepository.setProbationDate(newTime, chatId);
    }

    @Override
    public void insertContact(Message message) {
        Client client = clientRepository.getClientByChatId(message.chat().id());
            client.setLastName(message.contact().lastName());
            client.setPhone(message.contact().phoneNumber());
        clientRepository.save(client);
        logger.info("Contact {} was saved", client);
    }

    @Override
    public List<Client> getListOfClientsWithAnimal() {
        return clientRepository.getListOfClientsWithAnimal(currentTime);
    }

    @Override
    public List<Client> getListOfClientsWithAnimalAndProbationIsEnd() {
        return clientRepository.getListOfClientsWithAnimalAndProbationIsEnd(currentTime);
    }

    @Override
    public void setAdoptionStatus(int newStatus, Long clientChatId) {
        clientRepository.setAdoptionStatus(newStatus, clientChatId);
    }

    @Override
    public void addProbationDays(int countOfDays, Long clientChatId) {
        clientRepository.addProbationDays(currentTime.plusDays(countOfDays), clientChatId);
    }

    @Override
    public Client getClientByChatId(Long clientChatId) {
        return clientRepository.getClientByChatId(clientChatId);
    }
}
