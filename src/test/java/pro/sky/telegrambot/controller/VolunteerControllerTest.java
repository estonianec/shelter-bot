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
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.repository.VolunteerRepository;
import pro.sky.telegrambot.service.impl.VolunteerServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VolunteerController.class)
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VolunteerRepository volunteerRepository;

    @SpyBean
    private VolunteerServiceImpl volunteerService;

    @InjectMocks
    private VolunteerController volunteerController;

    private Volunteer volunteer;
    private JSONObject jsonObject;
    private List<Volunteer> volunteers;

    @BeforeEach
    void setUpData() throws JSONException {
        volunteer = new Volunteer();
        volunteer.setChatId(1L);
        volunteer.setUserName("TestName");
        volunteer.setWorking(true);

        jsonObject = new JSONObject();
        jsonObject.put("chatId", volunteer.getChatId());
        jsonObject.put("userName", volunteer.getUserName());
        jsonObject.put("isWorking", volunteer.isWorking());

        Volunteer volunteer2 = new Volunteer();
        volunteer2.setChatId(2L);
        volunteer2.setUserName("TestName2");
        volunteer2.setWorking(true);
        volunteers = List.of(volunteer, volunteer2);
    }

    @Test
    void createVolunteer() throws Exception {
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/admin/volunteer")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(volunteer.getChatId()))
                .andExpect(jsonPath("$.userName").value(volunteer.getUserName()))
                .andExpect(jsonPath("$.working").value(volunteer.isWorking()));
    }

    @Test
    void editVolunteer() throws Exception {
        Volunteer newVolunteer = new Volunteer();
        newVolunteer.setChatId(1L);
        newVolunteer.setUserName("123");
        newVolunteer.setWorking(false);
        when(volunteerRepository.getVolunteerByChatId(any())).thenReturn(volunteer);
        when(volunteerRepository.save(any(Volunteer.class))).thenReturn(newVolunteer);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/admin/volunteer")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chatId").value(newVolunteer.getChatId()))
                .andExpect(jsonPath("$.userName").value(newVolunteer.getUserName()))
                .andExpect(jsonPath("$.working").value(newVolunteer.isWorking()));
    }

    @Test
    void deleteVolunteer() throws Exception {
        doNothing().when(volunteerRepository).deleteById(any());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/admin/volunteer/" + volunteer.getChatId()))
                .andExpect(status().isOk());
    }

    @Test
    void getAllVolunteers() throws Exception {
        when(volunteerRepository.getAllBy()).thenReturn(volunteers);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/admin/volunteer/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(volunteers)));
    }
}