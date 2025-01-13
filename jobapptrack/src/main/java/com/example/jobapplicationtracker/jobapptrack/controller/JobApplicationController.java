package com.example.jobapplicationtracker.jobapptrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jobapplicationtracker.jobapptrack.model.ApplicationStatus;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import com.example.jobapplicationtracker.jobapptrack.service.JobApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/jobapplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping("/")
    public String homePage() {
        return "Welcome to the Job Application Tracker!";
    }

    @GetMapping
    public List<JobApplication> getAllApplications() {
        return service.getAllApplications();
    }

    @GetMapping("/status/{status}")
    public List<JobApplication> getApplicationsByStatus(@PathVariable ApplicationStatus status) {
        return service.getApplicationsByStatus(status);
    }


    @PostMapping
    public JobApplication addApplication(@RequestBody JobApplication jobApplication) {
        return service.saveApplication(jobApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        service.deleteApplication(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long id, @RequestBody JobApplication updatedJobApplication) {
        JobApplication updated = service.updateApplication(id, updatedJobApplication);
        return ResponseEntity.ok(updated);
    }

}
