package pro.sky.telegrambot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.service.ClientService;

import java.util.Collection;

@RestController
@RequestMapping("admin/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    @GetMapping
    public ResponseEntity<Collection<Client>> getAllClients() {
        Collection<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(clients);
    }

    @PutMapping
    public ResponseEntity<Client> editClient(@RequestBody Client client) {
        Client entity = clientService.editClient(client);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(entity);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Client> deleteClient(@RequestParam("id") long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
