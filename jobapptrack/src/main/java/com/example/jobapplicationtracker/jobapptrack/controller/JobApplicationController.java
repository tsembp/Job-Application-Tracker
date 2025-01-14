package com.example.jobapplicationtracker.jobapptrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jobapplicationtracker.jobapptrack.model.ApplicationStatus;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import com.example.jobapplicationtracker.jobapptrack.model.JobType;
import com.example.jobapplicationtracker.jobapptrack.service.JobApplicationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/jobapplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping
    public List<JobApplication> getAllApplications() {
        return service.getAllApplications();
    }

    /* ENDPOINTS */
    
    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplicationById(@PathVariable Long id){
        Optional<JobApplication> jobApp = service.getApplicationById(id);
        return jobApp.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<JobApplication> getApplicationsByStatus(@PathVariable ApplicationStatus status) {
        return service.getApplicationsByStatus(status);
    }

    @GetMapping("/jobType/{jobType}")
    public List<JobApplication> getApplicationByJobType(@PathVariable JobType jobType) {
        return service.getApplicationsByJobType(jobType);
    }

    @GetMapping("/location/{location}")
    public List<JobApplication> getApplicationByLocation(@PathVariable String location) {
        return service.getApplicationsByLocation(location);
    }

    @GetMapping("/search")
    public List<JobApplication> searchApplications(@RequestParam String keyword) {
        // find keyword in company, position, notes
        return service.getAllApplications().stream()
                // filter and check for both caps nad lowercase
                .filter(app -> (app.getCompany().toLowerCase().contains(keyword.toLowerCase())
                || app.getPosition().toLowerCase().contains(keyword.toLowerCase())
                || app.getNotes().toLowerCase().contains(keyword.toLowerCase())))
                .toList();
    }


    /* CRUD OPERATIONS */

    @PostMapping
    public ResponseEntity<?> addApplication(@Valid @RequestBody JobApplication jobApplication) {
        JobApplication savedApplication = service.saveApplication(jobApplication);
        return ResponseEntity.ok(savedApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        Optional<JobApplication> jobApp = service.getApplicationById(id);
        if (jobApp.isPresent()) {
            service.deleteApplication(id); // if exists -> delete
            return ResponseEntity.ok().body("Job Application deleted successfully.");
        } else {
            // handle 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Job Application with id `" + id + "` not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(@PathVariable Long id, @RequestBody JobApplication updatedJobApplication) {
        try {
            // update application record
            JobApplication updated = service.updateApplication(id, updatedJobApplication);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            // handle error 404 not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
