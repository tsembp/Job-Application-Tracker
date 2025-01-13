package com.example.jobapplicationtracker.jobapptrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jobapplicationtracker.jobapptrack.repository.JobApplicationRepository;
import com.example.jobapplicationtracker.jobapptrack.model.ApplicationStatus;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository; // use ready CRUD functions from JPA Repo

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    public Optional<JobApplication> getApplicationById(Long id) {
        return repository.findById(id);
    }      

    public JobApplication saveApplication(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id); // delete job with id
    }

    public List<JobApplication> getApplicationsByStatus(ApplicationStatus status) {
        return repository.findAll().stream()
                .filter(job -> job.getStatus() == status) // get jobs with matching status
                .toList();
    }

    public JobApplication updateApplication(Long id, JobApplication updatedJobApplication) {
        return repository.findById(id).map(existingApp -> { // find job with id and map it to `existingApp`
            // update job application details
            existingApp.setCompany(updatedJobApplication.getCompany());
            existingApp.setPosition(updatedJobApplication.getPosition());
            existingApp.setStatus(updatedJobApplication.getStatus());
            existingApp.setApplicationDate(updatedJobApplication.getApplicationDate());
            existingApp.setNotes(updatedJobApplication.getNotes());
            return repository.save(existingApp); // save job application
        }).orElseThrow(() -> new RuntimeException("Job Application not found with id " + id)); // handle not found
    }
    
}