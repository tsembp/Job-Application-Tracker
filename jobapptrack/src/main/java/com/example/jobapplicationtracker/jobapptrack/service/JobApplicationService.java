package com.example.jobapplicationtracker.jobapptrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.jobapplicationtracker.jobapptrack.repository.JobApplicationRepository;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;
import java.util.List;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository repository; // use ready CRUD functions from JPA Repo

    public List<JobApplication> getAllApplications() {
        return repository.findAll();
    }

    public JobApplication saveApplication(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    public void deleteApplication(Long id) {
        repository.deleteById(id);
    }
}