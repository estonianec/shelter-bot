package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
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

    /**
     * Создает клиента в БД
     * @param client новый клиент в виде JSON
     * @return созданный клиент
     */
    @PostMapping
    public Client createClient(@RequestBody Client client) {
        return clientService.createClient(client);
    }

    /**
     * Список <b>всех</b> существующих клиентов из БД
     *
     * @return коллекция клиентов
     */
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Поиск всех клиентов",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Client[].class)
                    )
            )
    })
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

    @DeleteMapping()
    public ResponseEntity<Client> deleteClient(@RequestParam("id") long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
