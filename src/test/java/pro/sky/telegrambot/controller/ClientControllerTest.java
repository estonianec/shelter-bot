package pro.sky.telegrambot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.telegrambot.model.Client;
import pro.sky.telegrambot.repository.ClientRepository;
import pro.sky.telegrambot.service.impl.ClientServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientRepository clientRepository;

    @SpyBean
    private ClientServiceImpl clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client;
    private JSONObject jsonObject;
    private List<Client> clients;

    @BeforeEach
    void setUpData() throws JSONException {
        client = new Client();
        client.setChatId(1L);
        client.setName("TestName");
        client.setLastName("TestLastName");
        client.setPhone("+7914");
        client.setAdoptionDate(LocalDateTime.now());
        client.setProbationDate(LocalDateTime.now().plusDays(30));
        client.setAdoptionStatus(1);

        jsonObject = new JSONObject();
        jsonObject.put("chatId", client.getChatId());
        jsonObject.put("name", client.getName());
        jsonObject.put("phone", client.getPhone());
        jsonObject.put("lastName", client.getLastName());
        jsonObject.put("adoptionDate", client.getAdoptionDate());
        jsonObject.put("probationDate", client.getProbationDate());
        jsonObject.put("adoptionStatus", client.getAdoptionStatus());

        Client client2 = new Client();
        client2.setChatId(2L);
        client2.setName("TestName2");
        client2.setLastName("TestLastName2");
        client2.setPhone("+7914222");
        client2.setAdoptionDate(LocalDateTime.now().plusDays(1));
        client2.setProbationDate(LocalDateTime.now().plusDays(31));
        client2.setAdoptionStatus(1);
        clients = List.of(client, client2);
    }


    @Test
    void createClient() throws Exception {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/client")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(client.getChatId()))
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.phone").value(client.getPhone()))
                .andExpect(jsonPath("$.lastName").value(client.getLastName()))
                .andExpect(jsonPath("$.adoptionDate").value(client.getAdoptionDate().toString().substring(0, client.getAdoptionDate().toString().length() - 2)))
                .andExpect(jsonPath("$.probationDate").value(client.getProbationDate().toString().substring(0, client.getAdoptionDate().toString().length() - 2)))
                .andExpect(jsonPath("$.adoptionStatus").value(client.getAdoptionStatus()));
    }


    @Test
    void getAllClients() throws Exception {
        when(clientRepository.findAll()).thenReturn(clients);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/admin/client/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clients)));
    }

    @Test
    void editClient() throws Exception {
        Client newClient = new Client();
        newClient.setChatId(1L);
        newClient.setName("TestName2");
        newClient.setLastName("TestLastName2");
        newClient.setPhone("+7914222");
        newClient.setAdoptionDate(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.DAYS));
        newClient.setProbationDate(LocalDateTime.now().plusDays(31).truncatedTo(ChronoUnit.DAYS));
        newClient.setAdoptionStatus(1);
        when(clientRepository.getClientByChatId(any())).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(newClient);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/client/")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(newClient.getChatId()))
                .andExpect(jsonPath("$.name").value(newClient.getName()))
                .andExpect(jsonPath("$.phone").value(newClient.getPhone()))
                .andExpect(jsonPath("$.lastName").value(newClient.getLastName()))
                .andExpect(jsonPath("$.adoptionDate").value(client.getAdoptionDate().toString().substring(0, client.getAdoptionDate().toString().length() - 2)))
                .andExpect(jsonPath("$.probationDate").value(client.getProbationDate().toString().substring(0, client.getAdoptionDate().toString().length() - 2)))
                .andExpect(jsonPath("$.adoptionStatus").value(newClient.getAdoptionStatus()));
    }

    @Test
    void deleteClient() throws Exception {
        doNothing().when(clientRepository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/client?id=" + client.getChatId()))
                .andExpect(status().isOk());
    }
}