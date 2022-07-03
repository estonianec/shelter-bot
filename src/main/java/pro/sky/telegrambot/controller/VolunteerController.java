package pro.sky.telegrambot.controller;

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

    @PostMapping
    public Volunteer createVolunteer(@RequestBody Volunteer volunteer) {
        return volunteerService.createVolunteer(volunteer);
    }
    @GetMapping
    public ResponseEntity<Collection<Volunteer>> getAllVolunteers() {
        Collection<Volunteer> volunteers = volunteerService.getAllVolunteers();
        if (volunteers.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(volunteers);
    }
    @PutMapping
    public ResponseEntity<Volunteer> editVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer entity = volunteerService.editVolunteer(volunteer);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(entity);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Volunteer> deleteVolunteer(@PathVariable("id") long id) {
        volunteerService.deleteVolunteer(id);
        return ResponseEntity.ok().build();
    }
}
