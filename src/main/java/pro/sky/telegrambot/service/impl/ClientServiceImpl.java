package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.model.Contact;
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
    public void saveClient(Contact contact) {
        Client client = new Client();
        client.setChatId(contact.userId());
        client.setFirstName(contact.firstName());
        client.setLastName(contact.lastName());
        client.setPhone(contact.phoneNumber());
        clientRepository.save(client);
    }
}
