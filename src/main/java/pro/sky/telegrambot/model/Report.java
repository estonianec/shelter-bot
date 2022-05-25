package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Report {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    LocalDateTime dateTimeOfReport;
    Long fileSize;
    String mediaType;
    byte[] data;
    String diet;
    String health;
    String behaviorChange;

    @ManyToOne
    Customer customer;
}
