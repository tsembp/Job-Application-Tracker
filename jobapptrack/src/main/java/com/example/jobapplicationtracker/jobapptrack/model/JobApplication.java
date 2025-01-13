package com.example.jobapplicationtracker.jobapptrack.model;

import org.hibernate.annotations.DynamicUpdate;

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
    private String status;
    private String applicationDate;
    private String notes;
    
}
