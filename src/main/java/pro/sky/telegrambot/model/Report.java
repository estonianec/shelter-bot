package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Report {
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    Long id;

    LocalDateTime dateTimeOfReport;
    Integer fileSize;
    String fileId;
    String description;

    @ManyToOne
    Client client;

    public Report() {
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

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String mediaType) {
        this.fileId = mediaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String diet) {
        this.description = diet;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return id.equals(report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
