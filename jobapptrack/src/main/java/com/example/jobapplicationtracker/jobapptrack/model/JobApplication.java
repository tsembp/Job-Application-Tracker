package com.example.jobapplicationtracker.jobapptrack.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String company;
    private String position;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate applicationDate;
    
    private String notes;

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    
    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}
