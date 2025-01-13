package com.example.jobapplicationtracker.jobapptrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jobapplicationtracker.jobapptrack.model.ApplicationStatus;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import com.example.jobapplicationtracker.jobapptrack.model.JobType;
import com.example.jobapplicationtracker.jobapptrack.service.JobApplicationService;

import java.util.List;
import java.util.Optional;

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
    public JobApplication addApplication(@RequestBody JobApplication jobApplication) {
        return service.saveApplication(jobApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        Optional<JobApplication> jobApp = service.getApplicationById(id);
        if (jobApp.isPresent()) {
            service.deleteApplication(id);
            return ResponseEntity.ok().body("Job Application deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Job Application with id `" + id + "` not found.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long id, @RequestBody JobApplication updatedJobApplication) {
        JobApplication updated = service.updateApplication(id, updatedJobApplication);
        return ResponseEntity.ok(updated);
    }

}
