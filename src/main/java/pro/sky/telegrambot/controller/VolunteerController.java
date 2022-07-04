package pro.sky.telegrambot.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambot.model.Volunteer;
import pro.sky.telegrambot.service.VolunteerService;

import java.util.Collection;

@RestController
@RequestMapping("admin/volunteer")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Создание волонтера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Volunteer.class)
                    )
            )
    })
    @PostMapping
    public Volunteer createVolunteer(@RequestBody Volunteer volunteer) {
        return volunteerService.createVolunteer(volunteer);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Получение списка всех волонтеров",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Volunteer[].class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<Collection<Volunteer>> getAllVolunteers() {
        Collection<Volunteer> volunteers = volunteerService.getAllVolunteers();
        if (volunteers.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(volunteers);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Изменение волонтера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Volunteer[].class)
                    )
            )
    })
    @PutMapping
    public ResponseEntity<Volunteer> editVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer entity = volunteerService.editVolunteer(volunteer);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(entity);
    }
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Удаление волонтера",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Volunteer[].class)
                    )
            )
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Volunteer> deleteVolunteer(@PathVariable("id") long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok().build();
    }
}
