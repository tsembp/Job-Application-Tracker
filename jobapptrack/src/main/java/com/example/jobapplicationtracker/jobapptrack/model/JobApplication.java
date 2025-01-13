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
    private String location;
    private String notes;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate applicationDate;
    

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
