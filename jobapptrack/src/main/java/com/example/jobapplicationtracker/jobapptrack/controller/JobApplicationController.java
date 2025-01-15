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
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

// @CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "http://127.0.0.1:3000")
@RestController
@RequestMapping("/api/jobapplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService service;

    @GetMapping
    public List<JobApplication> getAllApplications() {
        return service.getAllApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplicationById(@PathVariable Long id){
        Optional<JobApplication> jobApp = service.getApplicationById(id);
        return jobApp.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public List<JobApplication> filterApplications(@RequestParam(required = false) ApplicationStatus status, 
                                                    @RequestParam(required = false) JobType jobType, 
                                                    @RequestParam(required = false) String location, 
                                                    @RequestParam(required = false) String keyword) {
        return service.getAllApplications().stream()
                .filter(app -> (status == null || app.getStatus().equals(status)) &&
                            (jobType == null || app.getJobType().equals(jobType)) &&
                            (location == null || app.getLocation().equalsIgnoreCase(location)) &&
                            (keyword == null || 
                                app.getCompany().toLowerCase().contains(keyword.toLowerCase()) ||
                                app.getPosition().toLowerCase().contains(keyword.toLowerCase()) ||
                                app.getNotes().toLowerCase().contains(keyword.toLowerCase())))
                .toList();
    }

    
    /* FILTERING ENDPOINTS FOR TESTING PURPOSES */

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

    @PostMapping("/bulk")
    public ResponseEntity<?> addBulkApplications(@Valid @RequestBody List<JobApplication> jobApplications) {
        List<JobApplication> savedApplications = service.saveBulkApplication(jobApplications);
        return ResponseEntity.ok(savedApplications);
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

    /* FILE EXPORTING */
    @GetMapping("/export")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=applications.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Company,Position,Location,Notes,Status,Job Type,Application Date");

        List<JobApplication> applications = service.getAllApplications();
        for (JobApplication application : applications) {
            writer.println(
                "\"" + application.getCompany() + "\"," +
                "\"" + application.getPosition() + "\"," +
                "\"" + application.getLocation() + "\"," +
                "\"" + application.getNotes() + "\"," +
                "\"" + application.getStatus() + "\"," +
                "\"" + application.getJobType() + "\"," +
                "\"" + application.getApplicationDate() + "\""
            );
        }
        writer.flush();
        writer.close();
    }

}
