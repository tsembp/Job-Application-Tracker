package com.example.jobapplicationtracker.jobapptrack.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import lombok.Data;

@Entity
@Data
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company name is required.")
    private String company;

    @NotBlank(message = "Position is required.")
    private String position;

    @NotBlank(message = "Location is required.")
    private String location;

    private String notes;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Application status cannot be null.")
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Job type cannot be null.")
    private JobType jobType;

    @NotNull(message = "Application date cannot be null.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applicationDate;    

    public JobApplication(String company, String position, String location, String notes, ApplicationStatus status, JobType jobType, LocalDate applicationDate) {
        this.company = company;
        this.position = position;
        this.location = location;
        this.notes = notes;
        this.status = status;
        this.jobType = jobType;
        this.applicationDate = applicationDate;
    }

    public JobApplication() {
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public JobType getJobType(){
        return jobType;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getLocation(){
        return location;
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

    public void setJobType(JobType jobType){
        this.jobType = jobType;
    }

    public void setLocation(String location){
        this.location = location;
    }
    
}
