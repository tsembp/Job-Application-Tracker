package com.example.jobapplicationtracker.jobapptrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jobapplicationtracker.jobapptrack.repository.JobApplicationRepository;

import jakarta.persistence.EntityNotFoundException;

import com.example.jobapplicationtracker.jobapptrack.model.ApplicationStatus;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import com.example.jobapplicationtracker.jobapptrack.model.JobType;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository; // use ready CRUD functions from JPA Repo

    /* CRUD OPERATIONS */
    public JobApplication saveApplication(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id); // delete job with id
    }

    public JobApplication updateApplication(Long id, JobApplication updatedJobApplication) {
        return repository.findById(id).map(existingApp -> { // find job with id and map it to `existingApp`
            // update job application details
            existingApp.setCompany(updatedJobApplication.getCompany());
            existingApp.setPosition(updatedJobApplication.getPosition());
            existingApp.setStatus(updatedJobApplication.getStatus());
            existingApp.setJobType(updatedJobApplication.getJobType());
            existingApp.setLocation(updatedJobApplication.getLocation());
            existingApp.setApplicationDate(updatedJobApplication.getApplicationDate());
            existingApp.setNotes(updatedJobApplication.getNotes());
            return repository.save(existingApp); // save job application
        }).orElseThrow(() -> new EntityNotFoundException("Job Application not found with id " + id)); // handle not found
    }


    /* GETTERS FOR JOB APPLICATIONS */
    
    /**
     * Get application with id = 'id'
     */
    public Optional<JobApplication> getApplicationById(Long id) {
        return repository.findById(id);
    } 

    /**
     * Get all applications.
     */
    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    /**
     * Get applications that have specified status
     */
    public List<JobApplication> getApplicationsByStatus(ApplicationStatus status) {
        return repository.findAll().stream()
                .filter(job -> job.getStatus() == status) // get jobs with matching status
                .toList();
    }

    /**
     * Get applications that are of `jobType`
     */
    public List<JobApplication> getApplicationsByJobType(JobType jobType){
        return repository.findAll().stream()
                .filter(job -> job.getJobType() == jobType)
                .toList();
    }

    /**
     * Get jobs that are located at `location`
     */
    public List<JobApplication> getApplicationsByLocation(String location){
        return repository.findAll().stream()
                .filter(job -> job.getLocation().equalsIgnoreCase(location.toLowerCase()))
                .toList();
    }

}