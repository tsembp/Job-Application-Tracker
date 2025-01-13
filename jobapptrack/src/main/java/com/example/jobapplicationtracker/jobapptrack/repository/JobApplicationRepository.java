package com.example.jobapplicationtracker.jobapptrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jobapplicationtracker.jobapptrack.model.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}
