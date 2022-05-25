package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

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

    public Report(Long id, LocalDateTime dateTimeOfReport, Long fileSize, String mediaType, byte[] data, String diet, String health, String behaviorChange, Customer customer) {
        this.id = id;
        this.dateTimeOfReport = dateTimeOfReport;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.diet = diet;
        this.health = health;
        this.behaviorChange = behaviorChange;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeOfReport() {
        return dateTimeOfReport;
    }

    public void setDateTimeOfReport(LocalDateTime dateTimeOfReport) {
        this.dateTimeOfReport = dateTimeOfReport;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getBehaviorChange() {
        return behaviorChange;
    }

    public void setBehaviorChange(String behaviorChange) {
        this.behaviorChange = behaviorChange;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(id, report.id) && Objects.equals(dateTimeOfReport, report.dateTimeOfReport) && Objects.equals(fileSize, report.fileSize) && Objects.equals(mediaType, report.mediaType) && Arrays.equals(data, report.data) && Objects.equals(diet, report.diet) && Objects.equals(health, report.health) && Objects.equals(behaviorChange, report.behaviorChange) && Objects.equals(customer, report.customer);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, dateTimeOfReport, fileSize, mediaType, diet, health, behaviorChange, customer);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}
