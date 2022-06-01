package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Message;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

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
        if (message.contact() != null) {
            client.setLastName(message.contact().lastName());
            client.setPhone(message.contact().phoneNumber());
        }
        clientRepository.save(client);
    }
}
